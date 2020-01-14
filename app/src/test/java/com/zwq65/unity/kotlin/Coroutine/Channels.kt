package com.zwq65.unity.kotlin.Coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.junit.Test

/**
 * ================================================
 * 通道
 * <p>
 * Created by NIRVANA on 2020/1/8.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class Channels {

    /**
     * 通道
     *
     * 延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输. 通道提供了一种在流中传输值的方法.
     *
     * 通道基础:一个[Channel]是一个和 BlockingQueue 非常相似的概念.
     * 其中一个不同是它代替了阻塞的 put 操作并提供了挂起的[Channel.send],还替代了阻塞的 take 操作并提供了挂起的[Channel.receive].
     */
    @Test
    fun test1() = runBlocking<Unit>() {
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
    fun test2() = runBlocking<Unit>() {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close() // 我们结束发送
        }
// 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
        for (y in channel) println(y)
        println("Done!")
    }

    /**
     * 构建通道生产者
     *
     * 协程生成一系列元素的模式很常见. 这是 生产者——消费者 模式的一部分,并且经常能在并发的代码中看到它.
     * 你可以将生产者抽象成一个函数,并且使通道作为它的参数,但这与必须从函数中返回结果的常识相违悖.
     * 这里有一个名为[produce]的便捷的协程构建器,可以很容易的在生产者端正确工作, 并且我们使用扩展函数[consumeEach]在消费者端替代for循环
     */
    @Test
    fun test3() = runBlocking {
        val squares = produceSquares()
        squares.consumeEach { println(it) }
        println("Done!")
    }

    private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }

    /**
     * 管道
     *
     * 管道是一种一个协程在流中开始生产可能无穷多个元素的模式
     * 并且另一个或多个协程开始消费这些流,做一些操作,并生产了一些额外的结果. 在下面的例子中,对这些数字仅仅做了平方操作
     * 所有创建了协程的函数被定义在了[CoroutineScope]的扩展上, 所以我们可以依靠结构化并发来确保没有常驻在我们的应用程序中的全局协程.
     */
    @Test
    fun test4() = runBlocking {
        //启动并连接了整个管道

        // 从 1 开始生产整数
        val numbers = produceNumbers()
        // 对整数做平方
        val squares = square(numbers)
        // 打印前 5 个数字
        for (i in 1..5) println(squares.receive())
        // 我们的操作已经结束了
        println("Done!")
        // 取消子协程
        coroutineContext.cancelChildren()
    }

    private fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) send(x++) // 从 1 开始的无限的整数流
    }

    private fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) send(x * x)
    }

    /**
     * 在协程中使用一个管道来生成素数.我们开启了一个数字的无限序列.
     * 下面的例子打印了前十个素数, 在主线程的上下文中运行整个管道.
     * 直到所有的协程在该主协[runBlocking]的作用域中被启动完成.
     * 我们不必使用一个显式的列表来保存所有被我们已经启动的协程.我们使用[cancelChildren]扩展函数在我们打印了前十个素数以后来取消所有的子协程.
     *
     */
    @Test
    fun test5() = runBlocking {
        var cur = numbersFrom(2)
        /**
         * 现在我们开启了一个从 2 开始的数字流管道,从当前的通道中取一个素数, 并为每一个我们发现的素数启动一个流水线阶段：
         * numbersFrom(2) -> filter(2) -> filter(3) -> filter(5) -> filter(7) ……
         */
        for (i in 1..10) {
            val prime = cur.receive()
            println("prime:$prime")
            cur = filter(cur, prime)
        }
        // 取消所有的子协程来让主协程结束
        coroutineContext.cancelChildren()
    }

    /**
     * 在下面的管道阶段中过滤了来源于流中的数字,删除了所有可以被给定素数整除的数字.
     */
    private fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
        var x = start
        // 从 start 开始过滤整数流
        while (true) send(x++)
    }

    private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
        for (x in numbers) if (x % prime != 0) send(x)
    }

    /**
     * 扇出
     *
     * 多个协程也许会接收相同的管道，在它们之间进行分布式工作。
     * 注意，取消生产者协程会关闭它的通道，因此通过正在执行的生产者协程通道来终止迭代。
     * 还有，注意我们如何使用 for 循环显式迭代通道以在[launchProcessor]代码中执行扇出。
     * 与[consumeEach]不同，这个 for 循环是安全完美地使用多个协程的。如果其中一个生产者协程执行失败，其它的生产者协程仍然会继续处理通道;
     * 而通过[consumeEach]编写的生产者始终在正常或非正常完成时消耗（取消）底层通道。
     */
    @Test
    fun test6() = runBlocking {
        val producer = produceNumbers2()
        repeat(5) { launchProcessor(it, producer) }
        delay(950)
        producer.cancel() // 取消协程生产者从而将它们全部杀死
    }

    /**
     * start with a producer coroutine that is periodically producing integers (ten numbers per second)
     */
    private fun CoroutineScope.produceNumbers2() = produce<Int> {
        var x = 1 // start from 1
        while (true) {
            send(x++) // 产生下一个数字
            delay(100) // 等待 0.1 秒
        }
    }

    /**
     * Then we can have several processor coroutines. In this example, they just print their id and received number.
     */
    private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        for (msg in channel) {
            println("Processor #$id received $msg")
        }
    }

    /**
     * 扇入
     *
     * 多个协程可以发送到同一个通道。
     * 比如说，让我们创建一个字符串的通道，和一个在这个通道中以指定的延迟反复发送一个指定字符串的挂起函数：
     */
    @Test
    fun test7() = runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel, "foo", 200L) }
        launch { sendString(channel, "BAR!", 500L) }
        repeat(6) {
            // 接收前六个
            println(channel.receive())
        }
        // 取消所有子协程来让主协程结束
        coroutineContext.cancelChildren()
    }

    private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }

    /**
     * 带缓冲的通道
     *
     * 到目前为止展示的通道都是没有缓冲区的。无缓冲的通道在发送者和接收者相遇时传输元素。
     * 如果发送先被调用，则它将被挂起直到接收被调用， 如果接收先被调用，它将被挂起直到发送被调用。
     */
    @Test
    fun test8() = runBlocking {
        /**
         * [Channel]工厂函数与[produce]建造器通过一个可选的参数capacity来指定 缓冲区大小 。
         * 缓冲允许发送者在被挂起前发送多个元素， 就像 BlockingQueue 有指定的容量一样，当缓冲区被占满的时候将会引起阻塞。
         */
        val channel = Channel<Int>(4)
        // 启动带缓冲的通道
        val sender = launch {
            // 启动发送者协程
            repeat(10) {
                println("Sending $it")
                // 将在缓冲区被占满时挂起,前四个元素被加入到了缓冲区并且发送者在试图发送第五个元素的时候被挂起。
                channel.send(it)
            }
        }
        // 没有接收到东西……只是等待……
        delay(1000)
        // 取消发送者协程
        sender.cancel()
    }

    /**
     * 通道是公平的
     *
     * 发送和接收操作是 公平的 并且尊重调用它们的多个协程。
     * 它们遵守先进先出原则，可以看到第一个协程调用 receive 并得到了元素。
     * 在下面的例子中两个协程“乒”和“乓”都从共享的“桌子”通道接收到这个“球”元素。
     */
    @Test
    fun test9() = runBlocking {
        // 一个共享的 table（桌子）
        val table = Channel<Ball>()
        /**
         * “乒”协程首先被启动，所以它首先接收到了球。
         * 甚至虽然“乒” 协程在将球发送会桌子以后立即开始接收，但是球还是被“乓” 协程接收了，因为它一直在等待着接收球；
         * 注意，有时候通道执行时由于执行者的性质而看起来不那么公平。点击这个[https://github.com/Kotlin/kotlinx.coroutines/issues/111]来查看更多细节。
         */
        launch { player("ping", table) }
        launch { player("pong", table) }
        // 发球
        table.send(Ball(0))
        delay(1000) // 延迟 1 秒钟
        coroutineContext.cancelChildren()
    }

    suspend fun player(name: String, table: Channel<Ball>) {
        for (ball in table) { // 在循环中接收球
            ball.hits++
            println("$name $ball")
            delay(300) // 等待一段时间
            table.send(ball) // 将球发送回去
        }
    }

    data class Ball(var hits: Int)

    /**
     * 计时器通道
     *
     * 计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生[Unit]。
     * 虽然它看起来似乎没用，它被用来构建分段来创建复杂的基于时间的[produce]管道和进行窗口化操作以及其它时间相关的处理。
     */
    @Test
    fun test10() = runBlocking <Unit>{
        //创建计时器通道
        val tickerChannel = ticker(delayMillis = 1000, initialDelayMillis = 0)
        var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        // 初始尚未经过的延迟
        println("Initial element is available immediately: $nextElement")
        // 所有随后到来的元素都经过了 100 毫秒的延迟
        nextElement = withTimeoutOrNull(500) { tickerChannel.receive() }
        println("Next element is not ready in 500 ms: $nextElement")

        nextElement = withTimeoutOrNull(600) { tickerChannel.receive() }
        println("Next element is ready in 1000 ms: $nextElement")

        // 模拟大量消费延迟
        println("Consumer pauses for 1500ms")
        delay(1500)
        /**
         * 请注意，[ticker]可能知道消费者的暂停，默认情况下，如果发生暂停，则调整下一个生成的元素的延迟时间，尝试保持固定的生成元素速率。
         * 给可选的 mode 参数传入[TickerMode.FIXED_DELAY]可以保持固定元素之间的延迟。
         */
        // 下一个元素立即可用
        nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        println("Next element is available immediately after large consumer delay: $nextElement")
        // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
        nextElement = withTimeoutOrNull(600) { tickerChannel.receive() }
        println("Next element is ready in 500ms after consumer pause in 150ms: $nextElement")

        // 表明不再需要更多的元素
        tickerChannel.cancel()
    }

}
