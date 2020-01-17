package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import org.junit.Test
import java.io.IOException

/**
 * ================================================
 * 异常处理
 * <p>
 * Created by NIRVANA on 2020/1/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class `7ExceptionHandling` {

    /**
     * 异常的传播
     *
     * 协程构建器有两种风格：自动的传播异常（[launch] 以及 [actor]）或者将它们暴露给用户（[async]以及[produce]）.
     * 前者对待异常是不处理的,类似于Java的[Thread.UncaughtExceptionHandler],
     * 而后者依赖用户来最终消耗异常,比如说,通过[kotlinx.coroutines.Deferred.await]或[kotlinx.coroutines.channels.Channel.receive]
     */
    @Test
    fun test1() = runBlocking<Unit> {
        val job = GlobalScope.launch {
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException() // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
        }
        job.join()
        println("Joined failed job")
        val deferred = GlobalScope.async {
            println("Throwing exception from async")
            throw ArithmeticException() // 没有打印任何东西,依赖用户去调用等待
        }
        try {
            deferred.await()
            println("Unreached")
        } catch (e: ArithmeticException) {
            println("Caught ArithmeticException")
        }
    }

    /**
     *  [CoroutineExceptionHandler]上下文元素 用作协程的通用捕获块,在协程中可能发生自定义日志记录或异常处理.
     *  它和使用[Thread.UncaughtExceptionHandler]很相似.
     *  在JVM上，可以通过ServiceLoader注册[CoroutineExceptionHandler]来为所有协程重新定义全局异常处理程序.
     *  全局异常处理者就如同[Thread.UncaughtExceptionHandler]一样,当没有其他特定的处理程序注册时使用.
     *  在 Android 中,uncaughtExceptionPreHandler被设置在全局协程异常处理者中.
     *  仅在不希望用户处理的异常上调用[CoroutineExceptionHandler] ，因此在[async]中注册它等无效.
     */
    @Test
    fun test2() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }
        val job = GlobalScope.launch(handler) {
            throw AssertionError()
        }
        val deferred = GlobalScope.async(handler) {
            // 没有打印任何东西,依赖用户去调用 deferred.await()
            throw ArithmeticException()
        }
        joinAll(job, deferred)
    }

    /**
     *  取消与异常
     *
     *  取消与异常紧密相关.协程内部使用[CancellationException]来进行取消,这个异常会被所有的处理者忽略;
     *  因此,它们仅应用作其他调试信息的来源,可以通过catch块获取这些信息.
     *  使用[Job.cancel]取消协程时,协程终止,但不取消其父级.
     */
    @Test
    fun test3() = runBlocking<Unit> {
        val job = launch {
            val child = launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    println("Child is cancelled")
                }
            }
            yield()
            println("Cancelling child")
            child.cancel()
            child.join()
            yield()
            println("Parent is not cancelled")
        }
        job.join()
    }

    /**
     *  取消与异常
     *
     *  如果协程遇到除[CancellationException]以外的异常,它将取消具有该异常的父协程.
     *  这种行为不能被覆盖,且它被用来提供一个稳定的协程层次结构来进行结构化并发而无需依赖[CoroutineExceptionHandler]的实现.
     *  当父协程的所有子协程终止时,父协程将处理原始异常.
     *  这也是为什么,在这个例子中,[CoroutineExceptionHandler]总是被设置在由[GlobalScope]启动的协程中.
     *  将异常处理者设置在[runBlocking]主作用域内启动的协程中是没有意义的,因为主协程将始终在其子协程完成时被取消,尽管子协程设置了异常处理者.
     */
    @Test
    fun test4() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }
        val job = GlobalScope.launch(handler) {
            launch {
                // 第一个子协程
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    withContext(NonCancellable) {
                        println("Children are cancelled, but exception is not handled until all children terminate")
                        delay(100)
                        println("The first child finished its non cancellable block")
                    }
                }
            }
            launch {
                // 第二个子协程
                delay(10)
                println("Second child throws an exception")
                throw ArithmeticException()
            }
        }
        job.join()
    }

    /**
     *  异常聚合
     *
     *  如果协程的多个子协程抛出异常会怎样？
     *  The general rule is "the first exception wins" 因此第一个引发的异常暴露给处理程序.
     *  但这可能会导致丢失异常,例如,如果协程在其finally块中引发异常.因此会丢失其他异常.
     *  解决方案之一是分别报告每个异常
     *  但是[Deferred.await]应该具有相同的机制来避免不一致的行为,这将导致协程的实现细节（无论其是否将工作的一部分委派给其子代）泄漏给其异常处理程序
     */
    @Test
    fun test5() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception with suppressed ${exception.suppressed.contentToString()}")
        }
        val job = GlobalScope.launch(handler) {
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    throw ArithmeticException()
                }
            }
            launch {
                delay(100)
                throw IOException()
            }
            delay(Long.MAX_VALUE)
        }
        job.join()
    }

    /**
     *  取消异常是透明的并且会在默认情况下解包
     */
    @Test
    fun test6() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught original $exception")
        }
        val job = GlobalScope.launch(handler) {
            val inner = launch {
                launch {
                    launch {
                        throw IOException()
                    }
                }
            }
            try {
                inner.join()
            } catch (e: CancellationException) {
                //重新抛出CancellationException
                println("Rethrowing CancellationException with original cause")
                throw e
            }
        }
        job.join()
    }

    /**
     * 监督
     *
     * 正如我们之前研究的那样,取消是一种双向机制,在协程的整个层次结构之间传播.但是如果需要单向取消怎么办？
     * 此类需求的一个良好示例是在其作用域内定义作业的 UI 组件.如果任何一个 UI 的子作业执行失败了,它并不总是有必要取消（有效地杀死）整个 UI 组件
     * 但是如果 UI 组件被销毁了（并且它的作业也被取消了）,那么就必须使所有子工作都失败,因为不再需要他们的结果
     *
     * 监督作业
     * [SupervisorJob]可以被用于这些目的.它类似于常规的[Job],唯一的不同是：[SupervisorJob]的取消只会向下传播.
     */
    @Test
    fun test7() = runBlocking<Unit> {
        val supervisor = SupervisorJob()
        with(CoroutineScope(coroutineContext + supervisor)) {
            // 启动第一个子作业——这个示例将会忽略它的异常（不要在实践中这么做！）
            val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
                println("First child is failing")
                throw AssertionError("First child is cancelled")
            }
            // 启动第两个子作业
            val secondChild = launch {
                firstChild.join()
                // 取消了第一个子作业且没有传播给第二个子作业
                println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
                try {
                    delay(Long.MAX_VALUE)
                } catch (e: Exception) {
                    // 但是取消了监督的传播
                    println("Exception:$e")
                    println("Second child is cancelled because supervisor is cancelled")
                }
            }
            // 等待直到第一个子作业失败且执行完成
            firstChild.join()
            println("Cancelling supervisor")
            supervisor.cancel()
            secondChild.join()
        }
    }

    /**
     * 监督作用域
     *
     * 对于作用域的并发[supervisorScope]可以被用来替代[coroutineScope]来实现相同的目的.
     * 它只会单向的传播并且当作业自身执行失败的时候将所有子作业全部取消.
     * 作业自身也会在所有的子作业执行结束前等待, 就像[coroutineScope]所做的那样.
     */
    @Test
    fun test8() = runBlocking<Unit> {
        try {
            supervisorScope {
                val child = launch {
                    try {
                        println("Child is sleeping")
                        delay(Long.MAX_VALUE)
                    } finally {
                        println("Child is cancelled")
                    }
                }
                // 使用 yield 来给我们的子作业一个机会来执行打印
                yield()
                println("Throwing exception from scope")
                throw AssertionError()
            }
        } catch (e: AssertionError) {
            println("Caught assertion error")
        }
    }

    /**
     * 监督协程中的异常
     *
     * 常规的作业和监督作业之间的另一个重要区别是异常处理.
     * 监督协程中的每一个子作业应该通过异常处理机制处理自身的异常.
     * 这种差异来自于子作业的执行失败不会传播给它的父作业.
     */
    @Test
    fun test9() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }
        supervisorScope {
            val child = launch(handler) {
                println("Child throws an exception")
                throw AssertionError()
            }
            println("Scope is completing")
        }
        println("Scope is completed")
    }

}