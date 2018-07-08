package org.team2679.util

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class Notifier (action: () -> Unit, periodic: Long) {
    private var periodic: Long = periodic
    private var isRunning: Boolean = false
    private var task: () -> Unit

    init {
        this.task = action
    }

    fun start() {
        isRunning = true
        runBlocking {
            launch {
                while (isRunning) {
                    launch {
                        task()
                    }
                    delay(periodic)
                }
            }
        }
    }

    fun stop() {
        isRunning = false
    }
}