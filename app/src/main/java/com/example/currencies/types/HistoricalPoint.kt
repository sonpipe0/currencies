package com.example.currencies.types

data class HistoricalPoint (
    val currency: String,
    val base: String,
    val value: Float,
)