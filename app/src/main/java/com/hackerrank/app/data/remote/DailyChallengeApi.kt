package com.hackerrank.app.data.remote

import com.google.gson.Gson
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyChallengeApi
    @Inject
    constructor() {
        private val gson = Gson()

        suspend fun fetchToday(): DailyChallengeResponse? {
            return try {
                val json = URL(ENDPOINT).readText()
                val response = gson.fromJson(json, DailyChallengeResponse::class.java)
                if (response.date.isNotEmpty() && response.problemId.isNotEmpty()) response else null
            } catch (_: Exception) {
                null
            }
        }

        companion object {
            const val ENDPOINT = "https://raw.githubusercontent.com/suryamudti/hackerrank-app/main/daily-challenge.json"
        }
    }
