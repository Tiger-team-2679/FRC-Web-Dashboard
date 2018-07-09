package org.team2679.util.log

import kotlinx.coroutines.experimental.runBlocking
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.PrintWriter
import java.io.StringWriter

enum class Logger {
    INSTANCE;

    enum class ENTRY_TYPE {
        WARNING, EVENT, EXCEPTION
    }

    private val startTime = getTimeStamp()
    private var logPath = "/home/slowl0ris/FRC/"
    private var initiated = false

    private var handlers = ArrayList<LogHandler>()

    fun init(path: String) {
        logPath = path
        initiated = true
        runBlocking {
            try {
                PrintWriter(getLogFile(), "UTF-8").use { writer ->
                    writer.println("*** ROBOT LOG ***")
                    writer.println("*** START TIME : " + startTime + " ***")
                    writer.append("\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun logRobotInit() {
        logWrite("robot initiated", ENTRY_TYPE.EVENT)
    }

    fun logRobotSetup() {
        logWrite("robot setup has started", ENTRY_TYPE.EVENT)
    }

    fun logTeleopEvent() {
        logWrite("start of teleop", ENTRY_TYPE.EVENT)
    }

    fun logAutonomousEvent() {
        logWrite("start of auto", ENTRY_TYPE.EVENT)
    }

    fun logDisableEvent() {
        logWrite("robot disabled", ENTRY_TYPE.EVENT)
    }

    fun logEnableEvent() {
        logWrite("robot enabled", ENTRY_TYPE.EVENT)
    }

    fun logEvent(message: String) {
        logWrite(message, ENTRY_TYPE.EVENT)
    }

    fun logWarning(message: String) {
        logWrite(message, ENTRY_TYPE.WARNING)
    }

    fun logThrowException(exception: Exception) {
        logWrite(exception)
    }

    fun registerHandler(handler: LogHandler) {
        handlers.add(handler)
    }

    fun removeHandler(handler: LogHandler) {
        handlers.remove(handler)
    }

    private fun getTimeStamp(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd/HH:mm:ss")
        val formatted = current.format(formatter)

        return formatted
    }

    private fun logWrite(string: String, type: ENTRY_TYPE) {
        runBlocking {
            if (initiated) {
                var time = getTimeStamp()
                var message = string;
                var formatted = " > [" + time + "]" + " " + type.toString().toUpperCase() + " >> " + message;
                try {
                    PrintWriter(FileWriter(getLogFile(), true)).use { writer ->
                        writer.println(formatted)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                handlers.forEach {
                    it.onLog(ENTRY_TYPE.EXCEPTION, message, formatted);
                }
            }
        }
    }

    private fun logWrite(exception: Throwable) {
        if (initiated) {
            runBlocking {
                var time = getTimeStamp()
                var exceptionType = ""
                if (exception.javaClass.name != Exception::class.java.name) {
                    exceptionType = ": " + exception.javaClass.simpleName.toUpperCase()
                }
                val sw = StringWriter()
                val pw = PrintWriter(sw, true)
                exception.printStackTrace(pw)
                var message = sw.buffer.toString()
                var formatted = " > [" + time + "]" + " " + ENTRY_TYPE.EXCEPTION + exceptionType + " >> " + message;

                try {
                    PrintWriter(FileWriter(getLogFile(), true)).use { writer ->
                        writer.println(formatted)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                handlers.forEach {
                    it.onLog(ENTRY_TYPE.EXCEPTION, message, formatted);
                }
            }
        }
    }

    fun getLogFile(): String {
        if (initiated) {
            return logPath + "/RobotLog.txt"
        } else {
            return ""
        }
    }
}