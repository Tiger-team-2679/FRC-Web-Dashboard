package org.team2679.util.log

import kotlinx.coroutines.experimental.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

object Logger {

    private val startTime = getTimeStamp()
    private val path = "/home/slowl0ris/FRC/"

    private var handlers = ArrayList<LogHandler>()

    enum class ENTRY_TYPE {
        WARNING, EVENT, EXCEPTION
    }

    init {
        logSetup()
    }

    fun logRobotInit() {
        logWrite("robot initiated", ENTRY_TYPE.EVENT)
    }

    fun logRobotSetup()
    {
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

    fun logThrowException(exception: Exception) {
        logWrite(exception)
    }

    public fun registerHandler(handler: LogHandler){
        this.handlers.add(handler)
    }

    public fun removeHandler(handler: LogHandler){
        this.handlers.remove(handler)
    }

    private fun getTimeStamp(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm:ss")
        val formatted = current.format(formatter)

        return formatted
    }

    private fun logSetup() {
        runBlocking {
            try {
                PrintWriter(path + "/RobotLog_" + startTime + ".txt", "UTF-8").use { writer ->
                    writer.println("*** ROBOT LOG ***")
                    writer.println("*** START TIME : " + startTime + " ***")
                    writer.append("\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun logWrite(string: String, type: ENTRY_TYPE) {
        runBlocking {
            var time = getTimeStamp()
            var message = string;
            var formatted = "[" + time + "]" + " " + type.toString().toUpperCase() + " >> " + message;
            try {
                PrintWriter(FileWriter(path + "/RobotLog_" + startTime + ".txt", true)).use { writer ->
                    writer.println("[" + time + "]" + " " + type.toString().toUpperCase() + " >> " + string)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            handlers.forEach {
                it.onLog(ENTRY_TYPE.EXCEPTION, message, formatted);
            }
        }
    }

    private fun logWrite(exception: Exception) {
        runBlocking {
            var time = getTimeStamp()
            var exceptionType = ""
            if (exception.javaClass.name != Exception::class.java.name) {
                exceptionType = ": " + exception.javaClass.simpleName.toUpperCase()
            }
            var message = exception.message + " (" + exception.stackTrace.get(exception.stackTrace.lastIndex) + ")";
            var formatted = "[" + time + "]" + " " + ENTRY_TYPE.EXCEPTION + exceptionType + " >> " + message;

            try {
                PrintWriter(FileWriter(path + "/RobotLog_" + startTime + ".txt", true)).use { writer ->
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

    fun getLogFile(): String{
        return path + "/RobotLog_" + startTime + ".txt"
    }
}