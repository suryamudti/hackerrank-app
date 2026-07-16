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

    private lateinit var localeManager: LocaleManager

    @Before
    fun setUp() {
        every { context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs
    }

    @Test
    fun `default locale is english`() = runTest {
        every { sharedPreferences.getString("locale", "en") } returns "en"

        localeManager = LocaleManager(context)

        assertEquals("en", localeManager.getCurrentCode())
        assertTrue(localeManager.isEnglish())
    }

    @Test
    fun `default locale falls back to en when preference is null`() = runTest {
        every { sharedPreferences.getString("locale", "en") } returns null

        localeManager = LocaleManager(context)

        assertEquals("en", localeManager.getCurrentCode())
        assertTrue(localeManager.isEnglish())
    }

    @Test
    fun `setLocale saves code and updates flow`() = runTest {
        every { sharedPreferences.getString("locale", "en") } returns "en"

        localeManager = LocaleManager(context)

        localeManager.setLocale("in")

        assertEquals("in", localeManager.getCurrentCode())
        assertFalse(localeManager.isEnglish())
        assertEquals("in", localeManager.currentLocale.first())
        verify { editor.putString("locale", "in") }
        verify { editor.apply() }
    }

    @Test
    fun `setLocale to english updates correctly`() = runTest {
        every { sharedPreferences.getString("locale", "en") } returns "in"

        localeManager = LocaleManager(context)

        localeManager.setLocale("en")

        assertEquals("en", localeManager.getCurrentCode())
        assertTrue(localeManager.isEnglish())
        assertEquals("en", localeManager.currentLocale.first())
        verify { editor.putString("locale", "en") }
        verify { editor.apply() }
    }

    @Test
    fun `getLocaleFromContext reads stored locale`() {
        every { context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.getString("locale", "en") } returns "in"

        val locale = LocaleManager.getLocaleFromContext(context)

        assertEquals("in", locale.language)
    }

    @Test
    fun `getLocaleFromContext defaults to en`() {
        every { context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.getString("locale", "en") } returns null

        val locale = LocaleManager.getLocaleFromContext(context)

        assertEquals("en", locale.language)
    }
}
