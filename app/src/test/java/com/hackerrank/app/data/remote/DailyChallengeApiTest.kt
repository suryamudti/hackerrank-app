package com.hackerrank.app.data.remote

import com.hackerrank.app.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class DailyChallengeApiTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val api = DailyChallengeApi()

    @Test
    fun `fetchToday returns null on network error`() = runTest {
        val result = api.fetchToday()
        assertNull(result)
    }
}
