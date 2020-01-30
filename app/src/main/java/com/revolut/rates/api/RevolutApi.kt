package com.revolut.rates.api

import com.revolut.rates.model.RateList
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {
    @GET("latest")
    suspend fun getRateList(@Query("base") base: String?): RateList
}