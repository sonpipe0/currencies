package com.example.currencies.requests

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.currencies.BuildConfig
import com.example.currencies.responses.HistoricalPointResponse
import com.example.currencies.responses.LatestRatesResponse
import com.example.currencies.responses.PairConversionResponse
import com.example.currencies.types.HistoricalPoint
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import java.time.LocalDate
import javax.inject.Inject


class ApiServiceImpl @Inject constructor() {
    private val apiKey = BuildConfig.API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://v6.exchangerate-api.com/v6/${apiKey}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLatestRates(
        context: Context,
        onSuccess: (LatestRatesResponse) -> Unit,
        base: String,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<LatestRatesResponse> = service.getLatestRates(base)
        //print uri


        call.enqueue(object : retrofit.Callback<LatestRatesResponse> {
            override fun onResponse(
                response: Response<LatestRatesResponse>?,
                retrofit: Retrofit?
            ) {
                loadingFinished()
                if (response?.isSuccess == true) {
                    val latestRatesResponse = response.body()
                    println(latestRatesResponse)
                    if (latestRatesResponse != null) {
                        onSuccess(latestRatesResponse)
                    } else {
                        println("latestRatesResponse null")
                        onFail()
                    }
                } else {
                    println("not success")
                    onFail()
                }
            }

            override fun onFailure(t: Throwable) {
                println(t.message)
                Toast.makeText(context, "Failed to fetch currencies", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistoricalPointsForCurrency(
        context: Context,
        from: LocalDate,
        to: LocalDate,
        currency: String,
        base: String,
        onSuccess: (List<HistoricalPoint>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val service: ApiService = retrofit.create(ApiService::class.java)

        val dates: List<LocalDate> = generateSequence(from) { it.plusDays(1) }
            .takeWhile { it <= to }
            .toList()

        val historicalPoints: MutableList<HistoricalPoint> = mutableListOf()
        for (date in dates) {
            val call: Call<HistoricalPointResponse> = service.getHistoricalPoint(
                currency,
                date.year.toInt(),
                date.monthValue.toInt(),
                date.dayOfMonth.toInt()
            )

            call.enqueue(object : Callback<HistoricalPointResponse> {
                override fun onResponse(
                    response: Response<HistoricalPointResponse>?,
                    retrofit: Retrofit?
                ) {
                    loadingFinished()
                    if (response?.isSuccess == true) {
                        val historicalPointResponse: HistoricalPointResponse = response.body()
                        val valueAtBase = historicalPointResponse.conversionRates[base.uppercase()]
                        val historicalPoint: HistoricalPoint = HistoricalPoint(
                            historicalPointResponse.baseCode,
                            base,
                            valueAtBase?.toFloat() ?: 0f
                        )
                        historicalPoints.add(historicalPoint)
                        if (historicalPoints.size == dates.size) {
                            onSuccess(historicalPoints)
                        }
                    } else {
                        onFail()
                    }
                }

                override fun onFailure(t: Throwable) {
                    Toast.makeText(context, "Failed to fetch currencies", Toast.LENGTH_SHORT).show()
                    onFail()
                    loadingFinished()
                }
            })
        }
    }

    fun pairConversion(
        context: Context,
        from: String,
        to: String,
        onSuccess: (Double) -> Unit,
        onFail: () -> Unit
    ) {
        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<PairConversionResponse> = service.pairConversion(from, to)

        call.enqueue(object : Callback<PairConversionResponse> {
            override fun onResponse(
                response: Response<PairConversionResponse>?,
                retrofit: Retrofit?
            ) {
                if (response?.isSuccess == true) {
                    val conversionRate: PairConversionResponse = response.body()
                    onSuccess(conversionRate.conversionRate)
                } else {
                    onFail()
                }
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(context, "Failed to fetch currencies", Toast.LENGTH_SHORT).show()
                onFail()
            }
        })
    }


}