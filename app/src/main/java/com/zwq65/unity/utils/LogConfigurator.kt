package com.zwq65.unity.utils

import android.os.Environment
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.android.LogcatAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import org.slf4j.LoggerFactory
import java.io.File

/**
 * ================================================
 * 配置日志文件,每天生成一个新的日志文件
 *
 * Created by NIRVANA on 2019/5/6
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
object LogConfigurator {
    fun configure() {
        //log文件保存路径
        val logDir = Environment.getExternalStorageDirectory().toString() + File.separator + "logback"
        //log文件名前缀
        val filePrefix = "log"
        configureLogbackDirectly(logDir, filePrefix)
    }

    private fun configureLogbackDirectly(log_dir: String, filePrefix: String) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        context.reset()

        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.isAppend = true
        rollingFileAppender.context = context

        // OPTIONAL: Set an active log file (separate from the rollover files).
        // If rollingPolicy.fileNamePattern already set, you don't need this.
        //rollingFileAppender.setFile(LOG_DIR + "/log.txt");

        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.fileNamePattern = log_dir + "/" + filePrefix + "_%d{yyyyMMdd}.txt"
        rollingPolicy.maxHistory = 7
        rollingPolicy.setParent(rollingFileAppender)  // parent and context required!
        rollingPolicy.context = context
        rollingPolicy.start()

        rollingFileAppender.rollingPolicy = rollingPolicy

        val encoder = PatternLayoutEncoder()
        encoder.pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        encoder.context = context
        encoder.start()

        rollingFileAppender.encoder = encoder
        rollingFileAppender.start()

        val logcatAppender = LogcatAppender()
        logcatAppender.context = context
        logcatAppender.encoder = encoder
        logcatAppender.name = "logcat1"
        logcatAppender.start()

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.TRACE
        root.addAppender(rollingFileAppender)
        root.addAppender(logcatAppender)

        // print any status messages (warnings, etc) encountered in logback config
        //StatusPrinter.print(context);
    }


}