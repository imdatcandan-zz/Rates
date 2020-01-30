package com.revolut.rates.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RevolutRepository {

    val revolutApi: RevolutApi by lazy { createGithubApi() }

    private fun createGithubApi(): RevolutApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RevolutApi::class.java)
    }

    companion object {
        private const val BASE_URL = "https://revolut.duckdns.org/"
    }

}