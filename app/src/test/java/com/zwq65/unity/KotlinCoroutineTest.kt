package com.zwq65.unity

import com.blankj.utilcode.util.TimeUtils
import kotlinx.coroutines.*
import org.junit.Test

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2020/1/8.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class KotlinCoroutineTest {

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

    @Test
    fun test2() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            print("world")
        }
        print("Hello,")
        /**
         * 因为外部协程（runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。因此，我们可以在这个作用域中启动协程而无需显式 join 之.
         */
//        job.join()
    }

    @Test
    fun test3() = runBlocking {
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
    }

    fun print(content: String) {
        println("$content + ${TimeUtils.getNowMills()}")
    }
}
