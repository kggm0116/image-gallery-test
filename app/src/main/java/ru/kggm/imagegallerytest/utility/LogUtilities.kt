package ru.kggm.imagegallerytest.utility

import android.util.Log
import kotlin.system.measureTimeMillis

class LogUtilities {
    companion object {
        fun logCurrentThread(description: String) {
            Log.println(Log.ASSERT, "thread", "$description on thread #${Thread.currentThread().id}")
        }

        fun logExecutionTime(description: String, lambda: () -> Unit) {
            Log.println(Log.ASSERT, "execTime", "$description took #${measureTimeMillis { lambda() }}")
        }

        fun log(message: String) {
            Log.println(Log.ASSERT, "message", message)
        }
    }
}