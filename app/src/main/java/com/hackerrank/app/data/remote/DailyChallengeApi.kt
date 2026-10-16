package com.hackerrank.app.data.remote

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyChallengeApi
    @Inject
    constructor() {
        private val gson = Gson()

        suspend fun fetchToday(): DailyChallengeResponse? =
            withContext(Dispatchers.IO) {
                try {
                    val connection =
                        (URL(ENDPOINT).openConnection() as HttpURLConnection).apply {
                            connectTimeout = TIMEOUT_MS
                            readTimeout = TIMEOUT_MS
                            requestMethod = "GET"
                        }
                    val json = connection.inputStream.bufferedReader().use { it.readText() }
                    val response = gson.fromJson(json, DailyChallengeResponse::class.java)
                    if (response != null && response.date.isNotEmpty() && response.problemId.isNotEmpty()) {
                        response
                    } else {
                        null
                    }
                } catch (_: Exception) {
                    null
                }
            }

        companion object {
            const val ENDPOINT = "https://raw.githubusercontent.com/suryamudti/hackerrank-app/main/daily-challenge.json"
            const val TIMEOUT_MS = 5000
        }
    }
