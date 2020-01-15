package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2020/1/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class `7ExceptionHandling` {

    /**
     * 异常的传播
     *
     * 协程构建器有两种风格：自动的传播异常（[launch] 以及 [actor]）或者将它们暴露给用户（[async]以及[produce]）。
     * 前者对待异常是不处理的，类似于Java的[Thread.UncaughtExceptionHandler]，
     * 而后者依赖用户来最终消耗异常，比如说，通过[kotlinx.coroutines.Deferred.await]或[kotlinx.coroutines.channels.Channel.receive]
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
            throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
        }
        try {
            deferred.await()
            println("Unreached")
        } catch (e: ArithmeticException) {
            println("Caught ArithmeticException")
        }
    }

    /**
     *
     *  [CoroutineExceptionHandler]上下文元素被用来将通用的 catch 代码块用于在协程中自定义日志记录或异常处理。
     *  它和使用[Thread.UncaughtExceptionHandler]很相似。
     *  在 JVM 中可以重定义一个全局的异常处理者来将所有的协程通过 ServiceLoader 注册到 [CoroutineExceptionHandler]。
     *  全局异常处理者就如同[Thread.UncaughtExceptionHandler]一样，在没有更多的指定的异常处理者被注册的时候被使用。
     *  在 Android 中，uncaughtExceptionPreHandler被设置在全局协程异常处理者中。
     *  [CoroutineExceptionHandler] 仅在预计不会由用户处理的异常上调用， 所以在[async]构建器中注册它没有任何效果。
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
            // 没有打印任何东西，依赖用户去调用 deferred.await()
            throw ArithmeticException()
        }
        joinAll(job, deferred)
    }

    /**
     *  取消与异常
     *
     *  取消与异常紧密相关。协程内部使用[CancellationException]来进行取消，这个异常会被所有的处理者忽略;
     *  因此，它们仅应用作其他调试信息的来源，可以通过catch块获取这些信息。
     *  使用[Job.cancel]取消协程时，协程终止，但不取消其父级。
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
     *  如果协程遇到除[CancellationException]以外的异常，它将取消具有该异常的父协程。
     *  这种行为不能被覆盖，且它被用来提供一个稳定的协程层次结构来进行结构化并发而无需依赖[CoroutineExceptionHandler]的实现。
     *  当父协程的所有子协程终止时，父协程将处理原始异常。
     *  这也是为什么，在这个例子中，[CoroutineExceptionHandler]总是被设置在由[GlobalScope]启动的协程中。
     *  将异常处理者设置在[runBlocking]主作用域内启动的协程中是没有意义的，尽管子协程已经设置了异常处理者， 但是主协程也总是会被取消的。
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
}