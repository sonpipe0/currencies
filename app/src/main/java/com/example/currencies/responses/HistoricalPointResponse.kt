package com.example.currencies.responses

import com.google.gson.annotations.SerializedName

data class HistoricalPointResponse(
    val year: Int,
    val month: Int,
    val day: Int,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>,
): GeneralDocsResponse()