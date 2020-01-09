package com.zwq65.unity

import com.blankj.utilcode.util.TimeUtils
import kotlinx.coroutines.*
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
            delay(1000L)// 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
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
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    /**
     * 结构化的并发
     *
     * 协程的实际使用还有一些需要改进的地方. 当我们使用 GlobalScope.launch 时,我们会创建一个顶层协程.
     * 虽然它很轻量,但它运行时仍会消耗一些内存资源.如果我们忘记保持对新启动的协程的引用,它还会继续运行.如果协程中的代码挂起了会怎么样（例如,我们错误地延迟了太长时间）,如果我们启动了太多的协程并导致内存不足会怎么样？
     * 必须手动保持对所有已启动协程的引用并 join 之很容易出错.
     * 有一个更好的解决办法.我们可以在代码中使用结构化并发. 我们可以在执行操作所在的指定作用域内启动协程, 而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动.
     * 在我们的示例中,我们使用 runBlocking 协程构建器将 main 函数转换为协程. 包括 runBlocking 在内的每个协程构建器都将 CoroutineScope 的实例添加到其代码块所在的作用域中.
     * 我们可以在这个作用域中启动协程而无需显式 join 之,因为外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,可以将我们的示例简化为：
     */
    @Test
    fun test3() = runBlocking {
        launch {
            delay(1000L)
            print("world")
        }
        print("Hello,")
        /**
         * 因为外部协程（runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束.因此,我们可以在这个作用域中启动协程而无需显式 join 之.
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
     * 它启动了 10 万个协程,并且在一秒钟后,每个协程都输出一个点. 现在,尝试使用线程来实现.会发生什么？（很可能你的代码会产生某种内存不足的错误）
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
                println("I'm sleeping $i ...")
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
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
//        job.cancel() // 取消该作业
//        job.join() // 等待作业执行结束
        //挂起的函数cancelAndJoin()合并了对cancel()以及join()的调用.
        job.cancelAndJoin()
        println("main: Now I can quit.")
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
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
                //检测协程是否被取消 方法一：
                // yield()
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
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
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")
    }

    /**
     * 运行不能取消的代码块
     *
     * 在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出 [CancellationException],因为这里持续运行的代码是可以被取消的.
     * 通常,这并不是一个问题,所有良好的关闭操作（关闭一个文件、取消一个作业、或是关闭任何一种通信通道）通常都是非阻塞的,并且不会调用任何挂起函数.
     * 然而,在真实的案例中,当你需要挂起一个不被取消的协程,你可以将相应的代码包装在 withContext(NonCancellable) {……} 中,并使用 [withContext] 函数以及 [NonCancellable] 上下文
     */
    @Test
    fun test10() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    //// this code will not be cancelled
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
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
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        } catch (e: Exception) {
            withContext(NonCancellable) {
                //// this code will not be cancelled
                println("job: I'm running finally")
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
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // 在它运行得到结果之前取消它
        }
        println("Result is $result")
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
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")
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
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
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
             * 注意,如果我们只是在 println 中调用 await,而没有在单独的协程中调用 start,这将会导致顺序行为,
             * 直到 await 启动该协程 执行并等待至它结束,这并不是惰性的预期用例.
             * 在计算一个值涉及挂起函数时,这个 async(start = CoroutineStart.LAZY) 的用例用于替代标准库中的 lazy 函数.
             */
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    /**
     * async 风格的函数
     *
     * 我们可以定义异步风格的函数来 异步 的调用[doSomethingUsefulOne]和[doSomethingUsefulTwo]并使用[async]协程建造器并带有一个显式的[GlobalScope]]引用.
     * 我们给这样的函数的名称中加上“……Async”后缀来突出表明：事实上,它们只做异步计算并且需要使用延期的值来获得结果.
     * 注意,这些 xxxAsync 函数不是 挂起 函数.它们可以在任何地方使用. 然而,它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行.
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
                println("The answer is ${one.await() + two.await()}")
            }
        }
        /**
         * 这种带有异步函数的编程风格仅供参考,因为这在其它编程语言中是一种受欢迎的风格.在 Kotlin 的协程中使用这种风格是强烈不推荐的, 原因如下所述.
         * 考虑一下如果 val one = somethingUsefulOneAsync() 这一行和 one.await() 表达式这里在代码中有逻辑错误, 并且程序抛出了异常以及程序在操作的过程中中止,将会发生什么.
         * 通常情况下,一个全局的异常处理者会捕获这个异常,将异常打印成日记并报告给开发者,但是反之该程序将会继续执行其它操作.
         * 但是这里我们的 somethingUsefulOneAsync 仍然在后台执行, 尽管如此,启动它的那次操作也会被终止.
         */
        println("Completed in $time ms")
    }

    /**
     * 抛出了一个异常, 所有在作用域中启动的协程都会被取消.
     */
    @Test
    fun test17() = runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }
    /****************************************************************  协程上下文与调度器  ***********************************************************************************************/

    /**
     * 协程上下文包含一个协程调度器（参见 [CoroutineDispatcher]）它确定了哪些线程或与线程相对应的协程执行.
     * 协程调度器可以将协程限制在一个特定的线程执行,或将它分派到一个线程池,亦或是让它不受限地运行.
     * 所有的协程构建器诸如[launch]和[async]接收一个可选的CoroutineContext参数,它可以被用来显式的为一个新协程或其它上下文元素指定一个调度器.
     */
    @Test
    fun test18() = runBlocking<Unit> {
        /**
         * 当调用 launch { …… } 时不传参数,它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）.
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
     * 非受限的调度器非常适用于执行不消耗 CPU 时间的任务,以及不更新局限于特定线程的任何共享数据（如UI）的协程.
     * 另一方面,该调度器默认继承了外部的 [CoroutineScope]. [runBlocking]协程的默认调度器,
     * 特别是, 当它被限制在了调用者线程时,继承自它将会有效地限制协程在该线程运行并且具有可预测的 FIFO 调度.
     */
    @Test
    fun test19() = runBlocking<Unit> {
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
     * 协程的 Job 是上下文的一部分，并且可以使用 coroutineContext [Job] 表达式在上下文中检索它
     * 请注意，CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。
     */
    @Test
    fun test21() = runBlocking<Unit> {
        println("My job is ${coroutineContext[Job]}")
    }

    /**
     * 子协程
     *
     * 当一个协程被其它协程在[CoroutineScope]中启动的时候， 它将通过[CoroutineScope.coroutineContext]来承袭上下文，
     * 并且这个新协程的[Job]将会成为父协程作业的 子 作业。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
     * 然而，当使用[GlobalScope]来启动一个协程时，则新协程的作业没有父作业。 因此它与这个启动的作用域无关且独立运作。
     */
    @Test
    fun test22() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求（request）
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
        request.cancel() // 取消请求（request）的执行
        delay(1000) // 延迟一秒钟来看看发生了什么
        println("main: Who has survived request cancellation?")
    }

    /***************************************************************************************************************************************************************/

    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    /**
     * 抛出了一个异常, 所有在作用域中启动的协程都会被取消.
     * 请注意,如果其中一个子协程（即 two）失败,第一个 async 以及等待中的父协程都会被取消.
     */
    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
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
