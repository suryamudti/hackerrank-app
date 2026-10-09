package com.hackerrank.app.core.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class AppLogTest {
    @Before
    fun setUp() {
        AppLog.isEnabled = true
        ShadowLog.clear()
    }

    @After
    fun tearDown() {
        AppLog.isEnabled = true
        ShadowLog.clear()
    }

    @Test
    fun d_logsDebugMessageWhenEnabled() {
        AppLog.d("TestTag", "Debug message")
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Debug message", logs[0].msg)
    }

    @Test
    fun i_logsInfoMessageWhenEnabled() {
        AppLog.i("TestTag", "Info message")
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Info message", logs[0].msg)
    }

    @Test
    fun w_logsWarningMessageWithoutThrowable() {
        AppLog.w("TestTag", "Warning message")
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Warning message", logs[0].msg)
    }

    @Test
    fun w_logsWarningMessageWithThrowable() {
        val throwable = RuntimeException("Boom")
        AppLog.w("TestTag", "Warning with error", throwable)
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Warning with error", logs[0].msg)
        assertEquals(throwable, logs[0].throwable)
    }

    @Test
    fun e_logsErrorMessageWithoutThrowable() {
        AppLog.e("TestTag", "Error message")
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Error message", logs[0].msg)
    }

    @Test
    fun e_logsErrorMessageWithThrowable() {
        val throwable = IllegalStateException("Failed")
        AppLog.e("TestTag", "Error with throwable", throwable)
        val logs = ShadowLog.getLogsForTag("TestTag")
        assertEquals(1, logs.size)
        assertEquals("Error with throwable", logs[0].msg)
        assertEquals(throwable, logs[0].throwable)
    }

    @Test
    fun logs_notEmittedWhenDisabled() {
        AppLog.isEnabled = false
        AppLog.d("TestTag", "Debug")
        AppLog.i("TestTag", "Info")
        AppLog.w("TestTag", "Warn")
        AppLog.e("TestTag", "Error")

        val logs = ShadowLog.getLogsForTag("TestTag")
        assertTrue(logs.isEmpty())
    }
}
