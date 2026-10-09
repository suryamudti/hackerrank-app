package com.hackerrank.app.core.util

import android.util.Log

/**
 * Centralized logging utility for the application.
 * All application logging should go through this utility to ensure unified control,
 * test verification, and automated R8 log stripping in release builds.
 */
object AppLog {
    var isEnabled: Boolean = true

    fun d(
        tag: String,
        message: String,
    ) {
        if (isEnabled) {
            Log.d(tag, message)
        }
    }

    fun i(
        tag: String,
        message: String,
    ) {
        if (isEnabled) {
            Log.i(tag, message)
        }
    }

    fun w(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        if (isEnabled) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    fun e(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        if (isEnabled) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }
}
