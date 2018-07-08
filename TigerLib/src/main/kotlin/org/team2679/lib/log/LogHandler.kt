package org.team2679.util.log

interface LogHandler{

    fun onLog(type: Logger.ENTRY_TYPE, message: String, formatted: String);
}