package org.team2679.lib.log

import org.team2679.util.log.Logger

abstract  class LogRunnable(): Runnable{

    override fun run() {
        try{
            runLogged()
        }catch (exception: Exception){
            Logger.INSTANCE.logThrowException(exception)
            throw exception
        }
    }

    abstract fun runLogged();
}