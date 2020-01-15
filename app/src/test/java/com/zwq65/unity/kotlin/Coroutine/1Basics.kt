package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * ================================================
 * 基础
 * <p>
 * Created by NIRVANA on 2020/1/14.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class `1Basics` {

    /**
     * 协程是轻量级的线程. 它们在某些[CoroutineScope]上下文中与[launch]协程构建器 一起启动.
     * 这里我们在[GlobalScope]中启动了一个新的协程,这意味着新协程的生命周期只受整个应用程序的生命周期限制.
     */
    @Test
    fun test() {
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(1000L)// 非阻塞的等待 1 秒钟(默认时间单位是毫秒)
            println("world")// 在延迟后打印输出
        }
        println("hello")// 协程已在等待时主线程还在继续
//        方法一:
        Thread.sleep(2000L)// 阻塞主线程 2 秒钟来保证 JVM 存活,否则协程的delay()后的代码不会执行
//        方法二:
//        runBlocking {     // 但是这个表达式阻塞了主线程
//            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
//        }
    }

    /**
     * 这里的 runBlocking<Unit> { …… } 作为用来启动顶层主协程的适配器.
     */
    @Test
    fun test2() = runBlocking {
        // 开始执行主协程
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    /**
     * 结构化的并发
     *
     * 协程的实际使用还有一些需要改进的地方. 当我们使用[GlobalScope.launch]时,我们会创建一个顶层协程.
     * 虽然它很轻量,但它运行时仍会消耗一些内存资源.如果我们忘记保持对新启动的协程的引用,它还会继续运行.
     * 如果协程中的代码挂起了会怎么样(例如,我们错误地延迟了太长时间),如果我们启动了太多的协程并导致内存不足会怎么样？
     * 必须手动保持对所有已启动协程的引用并[Job.join]之很容易出错.
     * 有一个更好的解决办法.我们可以在代码中使用结构化并发. 我们可以在执行操作所在的指定作用域内启动协程, 而不是像通常使用线程(线程总是全局的)那样在[GlobalScope]中启动.
     * 在我们的示例中,我们使用[runBlocking]协程构建器将 main 函数转换为协程. 包括[runBlocking]在内的每个协程构建器都将[CoroutineScope]的实例添加到其代码块所在的作用域中.
     * 我们可以在这个作用域中启动协程而无需显式[Job.join]之,因为外部协程(示例中的[runBlocking])直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,可以将我们的示例简化为：
     */
    @Test
    fun test3() = runBlocking {
        launch {
            delay(1000L)
            println("world")
        }
        println("Hello,")
        /**
         * 因为外部协程([runBlocking])直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,我们可以在这个作用域中启动协程而无需显式[Job.join]之.
         */
//        job.join()
    }

    /**
     * 作用域构建器
     *
     * 除了由不同的构建器提供协程作用域之外,还可以使用[coroutineScope]构建器声明自己的作用域.它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束.
     * [runBlocking]与 [coroutineScope]可能看起来很类似,因为它们都会等待其协程体以及所有自协程结束.
     * 这两者的主要区别在于,[runBlocking]方法会阻塞当前线程来等待,而[coroutineScope]只是挂起,会释放底层线程用于其他用途.
     * 由于存在这点差异,[runBlocking]是常规函数,而[coroutineScope]是挂起函数.
     */
    @Test
    fun test4() = runBlocking {
        // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope {
            // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
//        doWorld()
    }

    /**
     * 协程很轻量
     * 它启动了 10 万个协程,并且在一秒钟后,每个协程都输出一个点. 现在,尝试使用线程来实现.会发生什么？(很可能你的代码会产生某种内存不足的错误)
     */
    @Test
    fun test5() = runBlocking {
        repeat(100_000) {
            // 启动大量的协程
            launch {
                delay(1000L)
                println(".")
            }
        }
    }

    /**
     * 全局协程像守护线程
     *
     * 在[GlobalScope]中启动的活动协程并不会使进程保活.它们就像守护线程.
     */
    @Test
    fun test6() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
        //在 GlobalScope 中启动的活动协程并不会使进程保活.它们就像守护线程.
    }

}