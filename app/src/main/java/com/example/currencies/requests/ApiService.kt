package com.example.currencies.requests

import com.example.currencies.responses.HistoricalPointResponse
import com.example.currencies.responses.LatestRatesResponse
import com.example.currencies.responses.PairConversionResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path

interface ApiService {
    @GET("latest/{base}")
    fun getLatestRates(@Path("base") base: String): Call<LatestRatesResponse>

    @GET("history/{base}/{year}/{month}/{date}")
    fun getHistoricalPoint(
        @Path("base") base: String,
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("date") date: Int
    ): Call<HistoricalPointResponse>

    @GET("pair/{from}/{to}")
    fun pairConversion(
        @Path("from") from: String,
        @Path("to") to: String
    ): Call<PairConversionResponse>
}