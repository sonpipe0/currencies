package com.example.currencies.search.types

data class CurrencyRate(
    val name: String,
    val code: String,
    val value: Double,
    val dailyChangePercentage: Double,
    val dailyChangeValue: Double
)

