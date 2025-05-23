package com.example.currencies.responses

import com.google.gson.annotations.SerializedName

data class LatestRatesResponse(
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>,
) : GeneralStatusResponse()
