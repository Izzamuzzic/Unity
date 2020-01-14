package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.withContext
import org.junit.Test

/**
 * ================================================
 * 协程上下文与调度器
 * <p>
 * Created by NIRVANA on 2020/1/14.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class `4ContextAndDispatchers` {

    /**
     * 协程上下文包含一个协程调度器(参见 [CoroutineDispatcher])它确定了哪些线程或与线程相对应的协程执行.
     * 协程调度器可以将协程限制在一个特定的线程执行,或将它分派到一个线程池,亦或是让它不受限地运行.
     * 所有的协程构建器诸如[launch]和[async]接收一个可选的CoroutineContext参数,它可以被用来显式的为一个新协程或其它上下文元素指定一个调度器.
     */
    @Test
    fun test1() = runBlocking<Unit> {
        /**
         * 当调用 launch { …… } 时不传参数,它从启动了它的 CoroutineScope 中承袭了上下文(以及调度器).
         * 在这个案例中,它从 main 线程中的 runBlocking 主协程承袭了上下文.
         */
        launch {
            // 运行在父协程的上下文中,即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * [Dispatchers.Unconfined] 是一个特殊的调度器且似乎也运行在 main 线程中,但实际上, 它是一种不同的机制,这会在后文中讲到.
         */
        launch(Dispatchers.Unconfined) {
            // 不受限的——将工作在主线程中
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * 默认调度器,当协程在[GlobalScope]中启动的时候使用, 它代表[Dispatchers.Default]使用了共享的后台线程池,
         * 所以GlobalScope.launch { …… }默认使用相同的调度器——launch(Dispatchers.Default) { …… }.
         */
        launch(Dispatchers.Default) {
            // 将会获取默认调度器
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * [newSingleThreadContext]为协程的运行启动了一个线程.一个专用的线程是一种非常昂贵的资源.
         * 在真实的应用程序中两者都必须被释放,当不再需要的时候,使用close 函数,或存储在一个顶层变量中使它在整个应用程序中被重用.
         */
        launch(newSingleThreadContext("MyOwnThread")) {
            // 将使它获得一个新的线程
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }

    /**
     * 非受限调度器 vs 受限调度器
     *
     * [Dispatchers.Unconfined]协程调度器在调用它的线程启动了一个协程,但它仅仅只是运行到第一个挂起点.
     * 挂起后,它恢复线程中的协程,而这完全由被调用的挂起函数来决定.
     * 非受限的调度器非常适用于执行不消耗 CPU 时间的任务,以及不更新局限于特定线程的任何共享数据(如UI)的协程.
     * 另一方面,该调度器默认继承了外部的 [CoroutineScope]. [runBlocking]协程的默认调度器,
     * 特别是, 当它被限制在了调用者线程时,继承自它将会有效地限制协程在该线程运行并且具有可预测的 FIFO 调度.
     */
    @Test
    fun test2() = runBlocking<Unit> {
        launch(Dispatchers.Unconfined) {
            // 非受限的——将和主线程一起工作
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch {
            // 父协程的上下文,主 runBlocking 协程
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
        /**
         * 该协程的上下文继承自 runBlocking {...} 协程并在 main 线程中运行,当 delay 函数调用的时候,非受限的那个协程在默认的执行者线程中恢复执行.
         * 非受限的调度器是一种高级机制,可以在某些极端情况下提供帮助而不需要调度协程以便稍后执行或产生不希望的副作用,
         * 因为某些操作必须立即在协程中执行. 非受限调度器不应该在通常的代码中使用.
         */
    }

    /**
     * 在不同线程间跳转
     *
     * 它演示了一些新技术.其中一个使用[runBlocking]来显式指定了一个上下文,
     * 并且另一个使用[withContext]函数来改变协程的上下文,而仍然驻留在相同的协程中.
     * 当我们不再需要某个在[newSingleThreadContext]中创建的线程的时候,它使用了Kotlin标准库中的 use 函数来释放该线程.
     */
    @Test
    fun test3() = runBlocking<Unit> {
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                runBlocking(ctx1) {
                    log("Started in ctx1")
                    withContext(ctx2) {
                        log("Working in ctx2")
                    }
                    log("Back to ctx1")
                }
            }
        }
    }

    /**
     * 上下文中的作业
     *
     * 协程的 Job 是上下文的一部分,并且可以使用 coroutineContext [Job] 表达式在上下文中检索它
     * 请注意,CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式.
     */
    @Test
    fun test4() = runBlocking<Unit> {
        println("My job is ${coroutineContext[Job]}")
    }

    /**
     * 子协程
     *
     * 当一个协程被其它协程在[CoroutineScope]中启动的时候, 它将通过[CoroutineScope.coroutineContext]来承袭上下文,
     * 并且这个新协程的[Job]将会成为父协程作业的 子 作业.当一个父协程被取消的时候,所有它的子协程也会被递归的取消.
     * 然而,当使用[GlobalScope]来启动一个协程时,则新协程的作业没有父作业. 因此它与这个启动的作用域无关且独立运作.
     */
    @Test
    fun test5() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求(request)
        val request = launch {
            // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation of the request")
            }
            // 另一个则承袭了父协程的上下文
            launch {
                delay(100)
                println("job2: I am a child of the request coroutine")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled")
            }
        }
        delay(500)
        request.cancel() // 取消请求(request)的执行
        delay(1000) // 延迟一秒钟来看看发生了什么
        println("main: Who has survived request cancellation?")
    }

    /**
     * 父协程的职责
     *
     * 一个父协程总是等待所有的子协程执行结束.父协程并不显式的跟踪所有子协程的启动,并且不必使用 Job.join 在最后的时候等待它们
     */
    @Test
    fun test6() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求(request)
        val request = launch {
            repeat(3) { i ->
                // 启动少量的子作业
                launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    println("Coroutine $i is done")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成,包括其所有子协程
        println("Now processing of the request is complete")
    }

    /**
     * 命名协程以用于调试
     *
     * 当协程经常打印日志并且你只需要关联来自同一个协程的日志记录时, 则自动分配的 id 是非常好的.
     * 然而,当一个协程与特定请求的处理相关联时或做一些特定的后台任务,最好将其明确命名以用于调试目的.
     * [CoroutineName]上下文元素与线程名具有相同的目的.当调试模式开启时,它被包含在正在执行此协程的线程名中.
     */
    @Test
    fun test7() = runBlocking<Unit> {
        log("Started main coroutine")
// 运行两个后台值计算
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }
        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log("Computing v2")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
    }

    /**
     * 组合上下文中的元素
     * 有时我们需要在协程上下文中定义多个元素.我们可以使用 + 操作符来实现.
     * 比如说,我们可以显式指定一个调度器来启动协程并且同时显式指定一个命名.
     */
    @Test
    fun test8() = runBlocking<Unit> {
        launch(Dispatchers.Default + CoroutineName("test")) {
            println("I'm working in thread ${Thread.currentThread().name}")
        }
    }

    /**
     * 协程作用域
     * 通过创建一个[CoroutineScope]实例来管理协程的生命周期,并使它与[Activity]的生命周期相关联.
     * [CoroutineScope]可以通过 CoroutineScope() 创建或者通过[MainScope]工厂函数.
     * 前者创建了一个通用作用域,而后者为使用[Dispatchers.Main]作为默认调度器的 UI 应用程序 创建作用域;
     * 或者,我们可以在这个[Activity]类中实现[CoroutineScope]接口.最好的方法是使用具有默认工厂函数的委托.
     * 我们也可以将所需的调度器与作用域合并(我们在这个示例中使用 Dispatchers.Default).
     */
    class Activity : CoroutineScope by CoroutineScope(Dispatchers.Default) {
        fun destroy() {
            cancel() // Extension on CoroutineScope
        }

        // 继续运行……
        // class Activity continues
        fun doSomething() {
            // 在示例中启动了 10 个协程,且每个都工作了不同的时长
            repeat(10) { i ->
                launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                    println("Coroutine $i is done")
                }
            }
        }
    } // Activity 类结束

    @Test
    fun test9() = runBlocking<Unit> {
        val activity = Activity()
        activity.doSomething() // 运行测试函数
        println("Launched coroutines")
        delay(1500L) // 延迟半秒钟
        println("Destroying activity!")
        activity.destroy() // 取消所有的协程
        delay(1000) // 为了在视觉上确认它们没有工作
    }

    /**
     * 线程局部数据
     *
     */
    @Test
    fun test10() = runBlocking<Unit> {
        val threadLocal = ThreadLocal<String?>() // 声明线程局部变量

        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    
    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

}