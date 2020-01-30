package com.revolut.rates.model

data class RateList(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)