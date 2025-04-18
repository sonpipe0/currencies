package com.example.currencies.responses

import com.google.gson.annotations.SerializedName

data class PairConversionResponse(
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("target_code")
    val targetCode: String,
    @SerializedName("conversion_rate")
    val conversionRate: Double,
): GeneralStatusResponse()