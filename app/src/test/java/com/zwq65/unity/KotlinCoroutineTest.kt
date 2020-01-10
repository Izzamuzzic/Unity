package com.zwq65.unity

import com.blankj.utilcode.util.TimeUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.text.SimpleDateFormat
import kotlin.system.measureTimeMillis

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2020/1/8.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class KotlinCoroutineTest {

    /****************************************************************  基础  ***********************************************************************************************/

    /**
     * 协程是轻量级的线程. 它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动.
     * 这里我们在 GlobalScope 中启动了一个新的协程,这意味着新协程的生命周期只受整个应用程序的生命周期限制.
     */
    @Test
    fun test() {
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(1000L)// 非阻塞的等待 1 秒钟(默认时间单位是毫秒)
            print("world")// 在延迟后打印输出
        }
        print("hello")// 协程已在等待时主线程还在继续
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
            print("World!")
        }
        print("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    /**
     * 结构化的并发
     *
     * 协程的实际使用还有一些需要改进的地方. 当我们使用 GlobalScope.launch 时,我们会创建一个顶层协程.
     * 虽然它很轻量,但它运行时仍会消耗一些内存资源.如果我们忘记保持对新启动的协程的引用,它还会继续运行.如果协程中的代码挂起了会怎么样(例如,我们错误地延迟了太长时间),如果我们启动了太多的协程并导致内存不足会怎么样？
     * 必须手动保持对所有已启动协程的引用并 join 之很容易出错.
     * 有一个更好的解决办法.我们可以在代码中使用结构化并发. 我们可以在执行操作所在的指定作用域内启动协程, 而不是像通常使用线程(线程总是全局的)那样在 GlobalScope 中启动.
     * 在我们的示例中,我们使用 runBlocking 协程构建器将 main 函数转换为协程. 包括 runBlocking 在内的每个协程构建器都将 CoroutineScope 的实例添加到其代码块所在的作用域中.
     * 我们可以在这个作用域中启动协程而无需显式 join 之,因为外部协程(示例中的 runBlocking)直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,可以将我们的示例简化为：
     */
    @Test
    fun test3() = runBlocking {
        launch {
            delay(1000L)
            print("world")
        }
        print("Hello,")
        /**
         * 因为外部协程(runBlocking)直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,我们可以在这个作用域中启动协程而无需显式 join 之.
         */
//        job.join()
    }

    /**
     * 作用域构建器
     *
     * 除了由不同的构建器提供协程作用域之外,还可以使用 coroutineScope 构建器声明自己的作用域.它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束.
     * runBlocking 与 coroutineScope 可能看起来很类似,因为它们都会等待其协程体以及所有自协程结束. 这两者的主要区别在于,runBlocking 方法会阻塞当前线程来等待,
     * 而 coroutineScope 只是挂起,会释放底层线程用于其他用途. 由于存在这点差异,runBlocking 是常规函数,而 coroutineScope 是挂起函数.
     */
    @Test
    fun test4() = runBlocking {
        // this: CoroutineScope
        launch {
            delay(200L)
            print("Task from runBlocking")
        }

        coroutineScope {
            // 创建一个协程作用域
            launch {
                delay(500L)
                print("Task from nested launch")
            }

            delay(100L)
            print("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        print("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
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
                print(".")
            }
        }
    }

    /**
     * 全局协程像守护线程
     *
     * 在 GlobalScope 中启动的活动协程并不会使进程保活.它们就像守护线程.
     */
    @Test
    fun test6() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                print("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
        //在 GlobalScope 中启动的活动协程并不会使进程保活.它们就像守护线程.
    }

    /****************************************************************  取消与超时  ***********************************************************************************************/

    /**
     * 取消协程的执行
     *
     * 一个长时间运行的应用程序中,你也许需要对你的后台协程进行细粒度的控制. 比如说,一个用户也许关闭了一个启动了协程的界面,那么现在协程的执行结果已经不再被需要了,
     * 这时,它应该是可以被取消的. 该 launch 函数返回了一个可以被用来取消运行中的协程的 Job：
     */
    @Test
    fun test7() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                print("job: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 延迟一段时间
        print("main: I'm tired of waiting!")
//        job.cancel() // 取消该作业
//        job.join() // 等待作业执行结束
        //挂起的函数cancelAndJoin()合并了对cancel()以及join()的调用.
        job.cancelAndJoin()
        print("main: Now I can quit.")
    }

    /**
     * 取消是协作的
     *
     * 协程的取消是 协作 的.一段协程代码必须协作才能被取消. 所有 [CoroutineScope] 中的挂起函数都是 可被取消的 .
     * 它们检查协程的取消, 并在取消时抛出 [CancellationException].
     * 然而,如果协程正在执行计算任务,并且没有检查取消的话,那么它是不能被取消的!
     */
    @Test
    fun test8() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            /**
             * 使计算代码可取消
             *
             * 我们有两种方法来使执行计算的代码可以被取消.第一种方法是定期调用挂起函数来检查取消.对于这种目的 [yield]yield 是一个好的选择.
             * 另一种方法是显式的检查取消状态.让我们试试第二种方法.
             */
            //检测协程是否被取消 方法二：
            while (isActive) { // 可以被取消的计算循环
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    print("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
                //检测协程是否被取消 方法一：
                // yield()
            }
        }
        delay(1300L) // 等待一段时间
        print("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        print("main: Now I can quit.")
    }

    /**
     * 在 finally 中释放资源
     *
     * 我们通常使用如下的方法处理在被取消时抛出 CancellationException 的可被取消的挂起函数.
     * 比如说,try {……} finally {……} 表达式以及 Kotlin 的 use 函数一般在协程被取消的时候执行它们的终结动作：
     */
    @Test
    fun test9() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    print("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                print("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        print("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        print("main: Now I can quit.")
    }

    /**
     * 运行不能取消的代码块
     *
     * 在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出 [CancellationException],因为这里持续运行的代码是可以被取消的.
     * 通常,这并不是一个问题,所有良好的关闭操作(关闭一个文件、取消一个作业、或是关闭任何一种通信通道)通常都是非阻塞的,并且不会调用任何挂起函数.
     * 然而,在真实的案例中,当你需要挂起一个不被取消的协程,你可以将相应的代码包装在 withContext(NonCancellable) {……} 中,并使用 [withContext] 函数以及 [NonCancellable] 上下文
     */
    @Test
    fun test10() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    print("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    //// this code will not be cancelled
                    print("job: I'm running finally")
                    delay(1000L)
                    print("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        print("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        print("main: Now I can quit.")
    }

    /**
     * 超时
     *
     * 在实践中绝大多数取消一个协程的理由是它有可能超时. 当你手动追踪一个相关 Job 的引用并启动了一个单独的协程在延迟后取消追踪,这里已经准备好使用[withTimeout]函数来做这件事
     * [withTimeout]抛出了 [TimeoutCancellationException],它是 [CancellationException] 的子类.
     */
    @Test
    fun test11() = runBlocking {
        try {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    print("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        } catch (e: Exception) {
            withContext(NonCancellable) {
                //// this code will not be cancelled
                print("job: I'm running finally")
            }
        }
    }

    /**
     * 由于取消只是一个例外,所有的资源都使用常用的方法来关闭. 如果你需要做一些各类使用超时的特别的额外操作,
     * 可以使用类似 [withTimeout] 的 [withTimeoutOrNull] 函数,并把这些会超时的代码包装在 try {...} catch (e: TimeoutCancellationException) {...} 代码块中,
     * 而 [withTimeoutOrNull] 通过返回 null 来进行超时操作,从而替代抛出一个异常
     */
    @Test
    fun test12() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                print("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // 在它运行得到结果之前取消它
        }
        print("Result is $result")
    }

    /****************************************************************  组合挂起函数  ***********************************************************************************************/

    /**
     * 默认顺序调用
     */
    @Test
    fun test13() = runBlocking {
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            print("The answer is ${one + two}")
        }
        print("Completed in $time ms")
    }

    /**
     * 使用 async 并发
     *
     * 在概念上,[async]就类似于[launch].它启动了一个单独的协程,这是一个轻量级的线程并与其它所有的协程一起并发的工作.
     * 不同之处在于 [launch] 返回一个 [Job] 并且不附带任何结果值,而 [async] 返回一个 [Deferred] —— 一个轻量级的非阻塞 future,
     * 这代表了一个将会在稍后提供结果的 promise.你可以使用 .await()在一个延期的值上得到它的最终结果, 但是 [Deferred] 也是一个 [Job],所以如果需要的话,你可以取消它.
     *
     *  请注意,使用协程进行并发总是显式的.
     */
    @Test
    fun test14() = runBlocking {
        val time = measureTimeMillis {
            val one = async {
                doSomethingUsefulOne()
            }
            val two = async {
                doSomethingUsefulTwo()
            }
            print("The answer is ${one.await() + two.await()}")
        }
        print("Completed in $time ms")
    }

    /**
     * 惰性启动的 async
     *
     * [async]可以通过将start参数设置为[CoroutineStart.LAZY]而变为惰性的. 在这个模式下,只有结果通过[await]获取的时候协程才会启动,或者在[Job]的[start]函数调用的时候.
     */
    @Test
    fun test15() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) {
                doSomethingUsefulOne()
            }
            val two = async(start = CoroutineStart.LAZY) {
                doSomethingUsefulTwo()
            }
            one.start()
            two.start()
            /**
             * 注意,如果我们只是在 print 中调用 await,而没有在单独的协程中调用 start,这将会导致顺序行为,
             * 直到 await 启动该协程 执行并等待至它结束,这并不是惰性的预期用例.
             * 在计算一个值涉及挂起函数时,这个 async(start = CoroutineStart.LAZY) 的用例用于替代标准库中的 lazy 函数.
             */
            print("The answer is ${one.await() + two.await()}")
        }
        print("Completed in $time ms")
    }

    /**
     * async 风格的函数
     *
     * 我们可以定义异步风格的函数来 异步 的调用[doSomethingUsefulOne]和[doSomethingUsefulTwo]并使用[async]协程建造器并带有一个显式的[GlobalScope]]引用.
     * 我们给这样的函数的名称中加上“……Async”后缀来突出表明：事实上,它们只做异步计算并且需要使用延期的值来获得结果.
     * 注意,这些 xxxAsync 函数不是 挂起 函数.它们可以在任何地方使用. 然而,它们总是在调用它们的代码中意味着异步(这里的意思是 并发 )执行.
     */
    @Test
    fun test16() = runBlocking {
        val time = measureTimeMillis {
            // 我们可以在协程外面启动异步执行
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()
            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候,这里我们使用 `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                print("The answer is ${one.await() + two.await()}")
            }
        }
        /**
         * 这种带有异步函数的编程风格仅供参考,因为这在其它编程语言中是一种受欢迎的风格.在 Kotlin 的协程中使用这种风格是强烈不推荐的, 原因如下所述.
         * 考虑一下如果 val one = somethingUsefulOneAsync() 这一行和 one.await() 表达式这里在代码中有逻辑错误, 并且程序抛出了异常以及程序在操作的过程中中止,将会发生什么.
         * 通常情况下,一个全局的异常处理者会捕获这个异常,将异常打印成日记并报告给开发者,但是反之该程序将会继续执行其它操作.
         * 但是这里我们的 somethingUsefulOneAsync 仍然在后台执行, 尽管如此,启动它的那次操作也会被终止.
         */
        print("Completed in $time ms")
    }

    /**
     * 抛出了一个异常, 所有在作用域中启动的协程都会被取消.
     */
    @Test
    fun test17() = runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            print("Computation failed with ArithmeticException")
        }
    }
    /****************************************************************  协程上下文与调度器  ***********************************************************************************************/

    /**
     * 协程上下文包含一个协程调度器(参见 [CoroutineDispatcher])它确定了哪些线程或与线程相对应的协程执行.
     * 协程调度器可以将协程限制在一个特定的线程执行,或将它分派到一个线程池,亦或是让它不受限地运行.
     * 所有的协程构建器诸如[launch]和[async]接收一个可选的CoroutineContext参数,它可以被用来显式的为一个新协程或其它上下文元素指定一个调度器.
     */
    @Test
    fun test18() = runBlocking<Unit> {
        /**
         * 当调用 launch { …… } 时不传参数,它从启动了它的 CoroutineScope 中承袭了上下文(以及调度器).
         * 在这个案例中,它从 main 线程中的 runBlocking 主协程承袭了上下文.
         */
        launch {
            // 运行在父协程的上下文中,即 runBlocking 主协程
            print("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * [Dispatchers.Unconfined] 是一个特殊的调度器且似乎也运行在 main 线程中,但实际上, 它是一种不同的机制,这会在后文中讲到.
         */
        launch(Dispatchers.Unconfined) {
            // 不受限的——将工作在主线程中
            print("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * 默认调度器,当协程在[GlobalScope]中启动的时候使用, 它代表[Dispatchers.Default]使用了共享的后台线程池,
         * 所以GlobalScope.launch { …… }默认使用相同的调度器——launch(Dispatchers.Default) { …… }.
         */
        launch(Dispatchers.Default) {
            // 将会获取默认调度器
            print("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        /**
         * [newSingleThreadContext]为协程的运行启动了一个线程.一个专用的线程是一种非常昂贵的资源.
         * 在真实的应用程序中两者都必须被释放,当不再需要的时候,使用close 函数,或存储在一个顶层变量中使它在整个应用程序中被重用.
         */
        launch(newSingleThreadContext("MyOwnThread")) {
            // 将使它获得一个新的线程
            print("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
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
    fun test19() = runBlocking<Unit> {
        launch(Dispatchers.Unconfined) {
            // 非受限的——将和主线程一起工作
            print("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            print("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch {
            // 父协程的上下文,主 runBlocking 协程
            print("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            print("main runBlocking: After delay in thread ${Thread.currentThread().name}")
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
    fun test20() = runBlocking<Unit> {
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
    fun test21() = runBlocking<Unit> {
        print("My job is ${coroutineContext[Job]}")
    }

    /**
     * 子协程
     *
     * 当一个协程被其它协程在[CoroutineScope]中启动的时候, 它将通过[CoroutineScope.coroutineContext]来承袭上下文,
     * 并且这个新协程的[Job]将会成为父协程作业的 子 作业.当一个父协程被取消的时候,所有它的子协程也会被递归的取消.
     * 然而,当使用[GlobalScope]来启动一个协程时,则新协程的作业没有父作业. 因此它与这个启动的作用域无关且独立运作.
     */
    @Test
    fun test22() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求(request)
        val request = launch {
            // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
            GlobalScope.launch {
                print("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                print("job1: I am not affected by cancellation of the request")
            }
            // 另一个则承袭了父协程的上下文
            launch {
                delay(100)
                print("job2: I am a child of the request coroutine")
                delay(1000)
                print("job2: I will not execute this line if my parent request is cancelled")
            }
        }
        delay(500)
        request.cancel() // 取消请求(request)的执行
        delay(1000) // 延迟一秒钟来看看发生了什么
        print("main: Who has survived request cancellation?")
    }

    /**
     * 父协程的职责
     *
     * 一个父协程总是等待所有的子协程执行结束.父协程并不显式的跟踪所有子协程的启动,并且不必使用 Job.join 在最后的时候等待它们
     */
    @Test
    fun test23() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求(request)
        val request = launch {
            repeat(3) { i ->
                // 启动少量的子作业
                launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    print("Coroutine $i is done")
                }
            }
            print("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成,包括其所有子协程
        print("Now processing of the request is complete")
    }

    /**
     * 命名协程以用于调试
     *
     * 当协程经常打印日志并且你只需要关联来自同一个协程的日志记录时, 则自动分配的 id 是非常好的.
     * 然而,当一个协程与特定请求的处理相关联时或做一些特定的后台任务,最好将其明确命名以用于调试目的.
     * [CoroutineName]上下文元素与线程名具有相同的目的.当调试模式开启时,它被包含在正在执行此协程的线程名中.
     */
    @Test
    fun test24() = runBlocking<Unit> {
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
    fun test25() = runBlocking<Unit> {
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
    fun test26() = runBlocking<Unit> {
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
    fun test27() = runBlocking<Unit> {
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

    /**
     * 序列
     *
     * 如果使用一些消耗 CPU 资源的阻塞代码计算数字(每次计算需要 100 毫秒)那么我们可以使用[Sequence]来表示数字
     */
    @Test
    fun test28() {
        foo().forEach { value -> println(value) }
    }

    private fun foo(): Sequence<Int> = sequence {
        //序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }

    /**
     * 挂起函数
     *
     * 然而,计算过程阻塞运行该代码的主线程.
     * 当这些值由异步代码计算时,我们可以使用[suspend]修饰符标记函数[foo],这样它就可以在不阻塞的情况下执行其工作并将结果作为列表返回.
     */
    @Test
    fun test29() = runBlocking<Unit> {
        foo2().forEach { value -> println(value) }
    }

    private suspend fun foo2(): List<Int> {
        delay(1000) // 假装我们在这里做了一些异步的事情
        return listOf(1, 2, 3)
    }

    /**
     * 流
     *
     * 使用[List]结果类型,意味着我们只能一次返回所有值.
     * 为了表示异步计算的值流（stream）,我们可以使用[Flow]类型（正如同步计算值会使用[Sequence]类型）
     */
    @Test
    fun test30() = runBlocking<Unit> {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // 收集这个流
        foo3().collect { value -> println(value) }
    }

    /**
     * 函数不再标有[suspend]修饰符
     */
    private fun foo3(): Flow<Int> = flow {
        // 流构建器
        //构建块中的代码可以挂起.
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    /**
     * 流是冷的
     *
     * [Flow]是一种类似于序列的冷流 — 这段[flow]构建器中的代码直到流被收集[collect]的时候才运行.
     */
    @Test
    fun test31() = runBlocking<Unit> {
        println("Calling foo...")
        val flow = foo4()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    /**
     * 这是返回一个流的[foo4]函数没有标记[suspend]修饰符的主要原因.
     * 通过它自己,[foo4]会尽快返回且不会进行任何等待.
     * 该流在每次收集的时候启动,这就是为什么当我们再次调用[collect]时我们会看到“Flow started”.
     */
    private fun foo4(): Flow<Int> = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    /**
     * 流取消
     *
     * 流采用与协程同样的协作取消.然而,流的基础设施未引入其他取消点.取消完全透明.
     * 像往常一样,流的收集可以在当流在一个可取消的挂起函数（例如[delay]）中挂起的时候取消,否则不能取消.
     * 下面的示例展示了当[withTimeoutOrNull]块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的.
     */
    @Test
    fun test32() = runBlocking<Unit> {
        withTimeoutOrNull(250) {
            // 在 250 毫秒后超时
            foo4().collect { value -> println(value) }
        }
        println("Done")
    }

    /**
     * 流构建器
     *
     * 先前示例中的 flow { ... } 构建器是最基础的一个.还有其它构建器使流的声明更简单：
     *
     * 1.使用 [flowOf] 构建器定义了一个发射固定值集的流.
     * 2.使用 [asFlow]扩展函数,可以将各种集合与序列转换为流.
     */
    @Test
    fun test33() = runBlocking<Unit> {
        flowOf(1, 2, 3).collect { value -> println(value) }
        (1..3).asFlow().collect { value -> println(value) }
    }

    /**
     * 过渡流操作符
     *
     * 可以使用操作符转换流,就像使用集合与序列一样.
     * 过渡操作符应用于上游流,并返回下游流.
     * 这些操作符也是冷操作符,就像流一样.这类操作符本身不是挂起函数.它运行的速度很快,返回新的转换流的定义.
     * 基础的操作符拥有相似的名字,比如 [map] 与 [filter].
     * 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数.
     * 举例来说,一个请求中的流可以使用 [map] 操作符映射出结果,即使执行一个长时间的请求操作也可以使用挂起函数来实现
     */
    @Test
    fun test34() = runBlocking<Unit> {
        (1..3).asFlow()
                .map { request ->
                    performRequest(request)
                }
                .collect { value -> println(value) }
    }

    private suspend fun performRequest(request: Int): String {
        delay(1000L)
        return "request$request"
    }

    /**
     * 转换操作符
     *
     * 在流转换操作符中,最通用的一种称为[transform].它可以用来模仿简单的转换,例如[map]与[filter],以及实施更复杂的转换.
     * 使用[transform]操作符,我们可以 发射 任意值任意次.
     * 比如说,使用[transform]我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应
     */
    @Test
    fun test35() = runBlocking<Unit> {
        (1..3).asFlow() // 一个请求流
                .transform { request ->
                    emit("Making request $request")
                    emit(performRequest(request))
                }
                .collect { response -> println(response) }
    }

    /**
     * 限长操作符
     *
     * 长过渡操作符（例如[take]）在流触及相应限制的时候会将它的执行取消.
     * 协程中的取消操作总是通过 抛出异常 来执行,这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行
     */
    @Test
    fun test36() = runBlocking<Unit> {
        numbers()
                .take(2) // 只获取前两个
                .collect { value -> println(value) }
    }

    private fun numbers(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            //协程中的取消操作总是通过 抛出异常 来执行,这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行
            println("Finally in numbers")
        }
    }

    /**
     * 末端流操作符
     *
     * Terminal operators on flows are suspending functions that start a collection of the flow.
     * [collect]是最基础的末端操作符,但是还有另外一些更方便使用的末端操作符：
     * - 转化为各种集合,例如[toList]与[toSet].
     * - 获取第一个（[first]）值与确保流发射单个（[single]）值的操作符.
     * - 使用[reduce]与[fold]将流规约到单个值.
     */
    @Test
    fun test37() = runBlocking<Unit> {
        val sum = (1..5).asFlow()
                .map { it * it } // 数字 1 至 5 的平方
                .reduce { a, b -> a + b } // 求和（末端操作符）
        println(sum)
    }

    /**
     * 流上下文
     *
     * 流的收集总是在调用协程的上下文中发生.
     * 例如,如果有一个流[foo5],然后以下代码在它的编写者指定的上下文中运行,而无论流[foo5]的实现细节如何:流的该属性称为 上下文保存 .
     * 所以默认的,flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中.
     * 举例来说,考虑打印线程的[foo5]的实现,它被调用并发射三个数字;
     * 由于 foo5().collect 是在主线程调用的,则[foo5]的流主体也是在主线程调用的.
     * 这是快速运行或异步代码的理想默认形式,它不关心执行的上下文并且不会阻塞调用者
     * flow {...} 构建器中的代码必须遵循上下文保存属性,并且  不允许从其他上下文中发射（[FlowCollector.emit]）.
     */
    @Test
    fun test38() = runBlocking<Unit> {
        foo5().collect { value -> log("Collected $value") }
    }

    private fun foo5(): Flow<Int> = flow {
        log("Started foo flow")
        for (i in 1..3) {
            emit(i)
        }
    }

    /**
     * 流的协程切换(更改流发射的上下文(线程))
     *
     * 长时间运行的消耗CPU的代码也许需要在[Dispatchers.Default]上下文中执行,并且更新 UI 的代码也许需要在[Dispatchers.Main]中执行.
     * 通常,[withContext]用于在 Kotlin 协程中改变代码的上下文,
     * 但是 flow {...} 构建器中的代码必须遵循上下文保存属性,并且  不允许从其他上下文中发射（[FlowCollector.emit]）,否则会抛出异常.
     *
     * flowOn操作符
     *
     * 例外的是[flowOn]函数,该函数用于更改流发射的上下文.
     * 以下示例展示了更改流上下文的正确方法,该示例还通过打印相应线程的名字以展示它们的工作方式
     */
    @Test
    fun test39() = runBlocking<Unit>() {
        foo6().collect { value ->
            log("Collected $value")
        }
    }

    private fun foo6(): Flow<Int> = flow {
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            log("Emitting $i")
            emit(i) // 发射下一个值
        }
    }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

    /**
     * 通道
     *
     * 延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输. 通道提供了一种在流中传输值的方法.
     *
     * 通道基础:一个[Channel]是一个和 BlockingQueue 非常相似的概念.
     * 其中一个不同是它代替了阻塞的 put 操作并提供了挂起的[Channel.send],还替代了阻塞的 take 操作并提供了挂起的[Channel.receive].
     */
    @Test
    fun test40() = runBlocking<Unit>() {
        val channel = Channel<Int>()
        launch {
            // 这里可能是消耗大量 CPU 运算的异步逻辑,我们将仅仅做 5 次整数的平方并发送
            for (x in 1..5) channel.send(x * x)
        }
        // 这里我们打印了 5 次被接收的整数：
        repeat(5) { println(channel.receive()) }
        println("Done!")
    }

    /**
     * 关闭与迭代通道
     *
     * 和队列不同,一个通道可以通过被关闭来表明没有更多的元素将会进入通道.
     * 在接收者中可以定期的使用 for 循环来从通道中接收元素.
     * 从概念上来说,一个 [Channel.close] 操作就像向通道发送了一个特殊的关闭指令.
     * 这个迭代停止就说明关闭指令已经被接收了.所以这里保证所有先前发送出去的元素都在通道关闭前被接收到.
     */
    @Test
    fun test41() = runBlocking<Unit>() {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close() // 我们结束发送
        }
// 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
        for (y in channel) println(y)
        println("Done!")
    }


    /***************************************************************************************************************************************************************/

    fun log(msg: String) = print("[${Thread.currentThread().name}] $msg")

    /**
     * 抛出了一个异常, 所有在作用域中启动的协程都会被取消.
     * 请注意,如果其中一个子协程(即 two)失败,第一个 async 以及等待中的父协程都会被取消.
     */
    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                print("First child was cancelled")
            }
        }
        val two = async<Int> {
            print("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    /**
     * 使用 async 的结构化并发
     *
     * 由于 async 被定义为了[CoroutineScope]上的扩展,我们需要将它写在作用域内,并且这是[coroutineScope]函数所提供的
     * 这种情况下,如果在[concurrentSum]函数内部发生了错误,并且它抛出了一个异常, 所有在作用域中启动的协程都会被取消.
     */
    suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }

    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    // 这是你的第一个挂起函数
    private suspend fun doWorld() {
        delay(1000L)
        print("World!")
    }

    private val formatter = SimpleDateFormat("SSS")

    private fun print(content: String) {
        println("$content + ${TimeUtils.getNowString(formatter)}")
    }

}
