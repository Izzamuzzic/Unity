package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test

/**
 * ================================================
 * 异步流
 * <p>
 * Created by NIRVANA on 2020/1/14.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@ExperimentalCoroutinesApi
class `5AsynchronousFlow` {

    /**
     * 序列
     *
     * 如果使用一些消耗 CPU 资源的阻塞代码计算数字(每次计算需要 100 毫秒)那么我们可以使用[Sequence]来表示数字
     */
    @Test
    fun test1() {
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
    fun test2() = runBlocking<Unit> {
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
    fun test3() = runBlocking<Unit> {
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
    fun test4() = runBlocking<Unit> {
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
    fun test5() = runBlocking<Unit> {
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
    fun test6() = runBlocking<Unit> {
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
    fun test7() = runBlocking<Unit> {
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
    fun test8() = runBlocking<Unit> {
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
    fun test9() = runBlocking<Unit> {
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
    fun test10() = runBlocking<Unit> {
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
    fun test11() = runBlocking<Unit> {
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
    fun test12() = runBlocking<Unit>() {
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

    fun log(msg: String) = print("[${Thread.currentThread().name}] $msg")

}