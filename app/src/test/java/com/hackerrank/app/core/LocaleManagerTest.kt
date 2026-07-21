package com.hackerrank.app.core

import android.content.Context
import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocaleManagerTest {
    private val context: Context = mockk()
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val storage = mutableMapOf<String, String>()

    private lateinit var localeManager: LocaleManager

    @Before
    fun setUp() {
        storage.clear()
        every { context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { sharedPreferences.getString(any(), any()) } answers {
            storage[firstArg()] ?: secondArg()
        }
        every { editor.putString(any(), any()) } answers {
            storage[firstArg()] = secondArg()
            editor
        }
        every { editor.apply() } just Runs
    }

    @Test
    fun `default locale is english`() =
        runTest {
            storage["locale"] = "en"

            localeManager = LocaleManager(context)

            assertEquals("en", localeManager.getCurrentCode())
            assertTrue(localeManager.isEnglish())
        }

    @Test
    fun `default locale falls back to en when preference is null`() =
        runTest {
            localeManager = LocaleManager(context)

            assertEquals("en", localeManager.getCurrentCode())
            assertTrue(localeManager.isEnglish())
        }

    @Test
    fun `setLocale saves code and updates flow`() =
        runTest {
            storage["locale"] = "en"

            localeManager = LocaleManager(context)

            localeManager.setLocale("in")

            assertEquals("in", storage["locale"])
            assertEquals("in", localeManager.getCurrentCode())
            assertFalse(localeManager.isEnglish())
            assertEquals("in", localeManager.currentLocale.first())
            verify { editor.putString("locale", "in") }
            verify { editor.apply() }
        }

    @Test
    fun `setLocale to english updates correctly`() =
        runTest {
            storage["locale"] = "in"

            localeManager = LocaleManager(context)

            localeManager.setLocale("en")

            assertEquals("en", storage["locale"])
            assertEquals("en", localeManager.getCurrentCode())
            assertTrue(localeManager.isEnglish())
            assertEquals("en", localeManager.currentLocale.first())
            verify { editor.putString("locale", "en") }
            verify { editor.apply() }
        }

    @Test
    fun `getLocaleFromContext reads stored locale`() {
        storage["locale"] = "in"

        val locale = LocaleManager.getLocaleFromContext(context)

        assertEquals("id", locale.language)
    }

    @Test
    fun `getLocaleFromContext defaults to en`() {
        val locale = LocaleManager.getLocaleFromContext(context)

        assertEquals("en", locale.language)
    }
}
