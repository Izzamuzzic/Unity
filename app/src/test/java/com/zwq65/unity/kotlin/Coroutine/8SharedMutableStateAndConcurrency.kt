package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * ================================================
 * 共享的可变状态与并发
 *
 * 协程可用多线程调度器（比如默认的[Dispatchers.Default]）并发执行.它提出了所有常见的并发问题.
 * 主要的问题是同步访问共享可变状态.
 * 在协程领域中,此问题的某些解决方案与多线程世界中的解决方案相似,但其他解决方案则是唯一的.
 * <p>
 * Created by NIRVANA on 2020/1/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class `8SharedMutableStateAndConcurrency` {

    /**
     * 问题
     *
     * 我们启动一百个协程,它们都做一千次相同的操作.
     * 我们同时会测量它们的完成时间以便进一步的比较
     */
    private suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100  // 启动的协程数量
        val k = 1000 // 每个协程重复执行同一动作的次数
        val time = measureTimeMillis {
            coroutineScope {
                // 协程的作用域
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }

    @Volatile // 在 Kotlin 中 `volatile` 是一个注解
    private var counter = 0

    /**
     * 我们从一个非常简单的动作开始：使用多线程的 Dispatchers.Default 来递增一个共享的可变变量.
     */
    @Test
    fun test1() = runBlocking<Unit> {
        withContext(Dispatchers.Default) {
            massiveRun {
                counter++
            }
        }
        /**
         * 它不太可能打印出“Counter = 100000”,因为一百个协程在多个线程中同时递增计数器但没有做并发处理.
         *
         * volatile 无济于事
         * 给[counter]加上[Volatile]后,这段代码运行速度更慢了,但我们最后仍然没有得到“Counter = 100000”这个结果,
         * 因为[Volatile]变量保证可线性化（这是“原子”的技术术语）读取和写入变量,但在大量动作（在我们的示例中即“递增”操作）发生时并不提供原子性.
         */
        println("Counter = $counter")
    }

    /**
     * 线程安全的数据结构
     * 一种对线程、协程都有效的常规解决方法,就是使用线程安全（也称为同步的、 可线性化、原子）的数据结构,
     * 它为需要在共享状态上执行的相应操作提供所有必需的同步处理.在简单的计数器场景中,
     * 我们可以使用具有[AtomicInteger.incrementAndGet]原子操作的[AtomicInteger]类
     * 这是针对此类特定问题的最快解决方案.它适用于普通计数器、集合、队列和其他标准数据结构以及它们的基本操作.
     * 然而,它并不容易被扩展来应对复杂状态、或一些没有现成的线程安全实现的复杂操作.
     */

    /**
     * 以细粒度限制线程
     *
     * 限制线程 是解决共享可变状态问题的一种方案：对特定共享状态的所有访问权都限制在单个线程中.
     * 它通常应用于 UI 程序中：所有 UI 状态都局限于单个事件分发线程或应用主线程中.
     * 这在协程中很容易实现,通过使用一个单线程上下文
     */
    @Test
    fun test2() = runBlocking<Unit> {
        val counterContext = newSingleThreadContext("CounterContext")

        withContext(Dispatchers.Default) {
            massiveRun {
                // 将每次自增限制在单线程上下文中
                withContext(counterContext) {
                    counter++
                }
            }
        }
        /**
         * 这段代码运行非常缓慢,因为它进行了 细粒度 的线程限制.
         * 每个增量操作都得使用 [withContext(counterContext)] 块从多线程 Dispatchers.Default 上下文切换到单线程上下文
         */
        println("Counter = $counter")
    }

    /**
     * 以粗粒度限制线程
     *
     * 在实践中,线程限制是在大段代码中执行的,例如：状态更新类业务逻辑中大部分都是限于单线程中.
     * 下面的示例演示了这种情况, 在单线程上下文中运行每个协程
     */
    @Test
    fun test3() = runBlocking<Unit> {
        val counterContext = newSingleThreadContext("CounterContext")

        // 将一切都限制在单线程上下文中
        withContext(counterContext) {
            massiveRun {
                counter++
            }
        }
        /**
         * 这段代码运行更快而且打印出了正确的结果.
         */
        println("Counter = $counter")
    }

    /**
     * 互斥
     *
     * 该问题的互斥解决方案：使用永远不会同时执行的 关键代码块 来保护共享状态的所有修改.
     * 在阻塞的世界中,你通常会为此目的使用[synchronized]或者 ReentrantLock. 在协程中的替代品叫做 Mutex .
     * 它具有 lock 和 unlock 方法, 可以隔离关键的部分.
     * 关键的区别在于 Mutex.lock() 是一个挂起函数,它不会阻塞线程.
     * 还有[withLock]扩展函数,可以方便的替代常用的 mutex.lock(); try { …… } finally { mutex.unlock() } 模式
     */
    @Test
    fun test4() = runBlocking<Unit> {
        val mutex = Mutex()
        withContext(Dispatchers.Default) {
            massiveRun {
                // 用锁保护每次自增
                mutex.withLock {
                    counter++
                }
            }
        }

        /**
         * 此示例中锁是细粒度的,因此会付出一些代价.
         * 但是对于某些必须定期修改共享状态的场景,它是一个不错的选择,
         * 但是没有自然线程可以限制此状态.
         */
        println("Counter = $counter")
    }

    /**
     * Actors
     *
     * [actor]是由协程、该协程所限制和封装的状态 以及 与其他协程进行通信的通道的组合 组成的实体
     * 可以将简单的actor编写为函数,但是状态复杂的actor更适合于类.
     * 有一个[actor]协程生成器,可以方便地将[actor]的邮箱Channel合并到其作用域中,以从中接收消息,并将send Channel合并到结果作业对象中
     * 因此,可以将对[actor]的单个引用作为其句柄持有
     *
     * 接下来我们定义一个函数,使用[actor]协程构建器来启动一个[actor]
     */
    // This function launches a new counter actor
    fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0 // actor state
        for (msg in channel) { // iterate over incoming messages
            when (msg) {
                is IncCounter -> counter++
                is GetCounter -> msg.response.complete(counter)
            }
        }
    }

    @Test
    fun test5() = runBlocking<Unit> {
        val counter = counterActor() // create the actor
        /**
         * [actor]本身在哪个上下文中执行无关紧要.actor是协程,并且协程按顺序执行
         * 因此将状态限制在特定的协程中可以解决共享的可变状态问题
         * 实际上,[actor]可以修改自己的私有状态,但只能通过消息互相影响（避免使用任何锁）.
         * [actor]在高负载下比锁更有效,因为在这种情况下它总是有工作要做,而且根本不需要切换到不同的上下文.
         * 注意,[actor]协程构建器是一个双重的[kotlinx.coroutines.channels.produce]协程构建器.
         * 一个[actor]与它接收消息的通道相关联,而一个[kotlinx.coroutines.channels.produce]与它发送元素的通道相关联.
         */
        withContext(Dispatchers.Default) {
            massiveRun {
                counter.send(IncCounter)
            }
        }
        // send a message to get a counter value from an actor
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response))
        println("Counter = ${response.await()}")
        counter.close() // shutdown the actor
    }

}

/**
 * 使用actor的第一步是定义actor将要处理的一类消息. Kotlin 的密封类[Sealed Classes]很适合这种场景
 * 我们使用[IncCounter]消息定义[CounterMsg]密封类以增加计数器,并使用[GetCounter]消息获取其值.后者需要发送响应.
 * 为此,此处使用了[CompletableDeferred]通信原语,该原语表示将来将要知道（传递）的单个值.
 */
// Message types for counterActor
sealed class CounterMsg

object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply