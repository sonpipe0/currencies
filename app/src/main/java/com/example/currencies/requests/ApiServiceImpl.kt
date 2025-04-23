package com.example.currencies.requests

import DayScheme
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.currencies.BuildConfig
import com.example.currencies.R
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

        call.enqueue(object : retrofit.Callback<LatestRatesResponse> {
            override fun onResponse(
                response: Response<LatestRatesResponse>?,
                retrofit: Retrofit?
            ) {
                loadingFinished()
                if (response?.isSuccess == true) {
                    val latestRatesResponse = response.body()
                    if (latestRatesResponse != null) {
                        onSuccess(latestRatesResponse)
                    } else {
                        onFail()
                    }
                } else {
                    onFail()
                }
            }

            override fun onFailure(t: Throwable) {
                println(t.message)
                Toast.makeText(context, R.string.failed_to_fetch_currencies, Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLastDayWeekOrMonthRates(
        context: Context,
        base: String,
        scheme: DayScheme,
        onSuccess: (LatestRatesResponse) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val service: ApiService = retrofit.create(ApiService::class.java)

        val date: LocalDate = when (scheme) {
            DayScheme.DAILY -> LocalDate.now().minusDays(1)
            DayScheme.WEEKLY -> LocalDate.now().with(java.time.DayOfWeek.MONDAY)
            DayScheme.QUARTERLY -> {
                val day = LocalDate.now().dayOfMonth
                val firstDayQuarter = when {
                    day <= 15 -> 1
                    else -> 15
                }
                LocalDate.of(LocalDate.now().year, LocalDate.now().month, firstDayQuarter)
            }

            DayScheme.MONTHLY -> LocalDate.now().withDayOfMonth(1)
        }

        val schemeStringError = when (scheme) {
            DayScheme.DAILY -> context.getString(R.string.failed_to_fetch_last_day_currencies)
            DayScheme.WEEKLY -> context.getString(R.string.failed_to_fetch_last_week_currencies)
            DayScheme.QUARTERLY -> context.getString(R.string.failed_to_fetch_last_quarter_currencies)
            DayScheme.MONTHLY -> context.getString(R.string.failed_to_fetch_last_month_currencies)
        }

        val call: Call<HistoricalPointResponse> = service.getHistoricalPoint(
            base,
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
                    val historicalResponse: HistoricalPointResponse = response.body()
                    val latestRatesResponse = LatestRatesResponse(
                        baseCode = historicalResponse.baseCode,
                        conversionRates = historicalResponse.conversionRates,
                    )
                    onSuccess(latestRatesResponse)
                } else {
                    onFail()
                }
            }

            override fun onFailure(t: Throwable) {
                println(t.message)
                Toast.makeText(
                    context,
                    schemeStringError,
                    Toast.LENGTH_SHORT
                ).show()
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
                    Toast.makeText(context,
                        context.getString(R.string.failed_to_fetch_currencies), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, R.string.failed_to_fetch_currencies, Toast.LENGTH_SHORT).show()
                onFail()
            }
        })
    }


}