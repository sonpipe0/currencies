package com.example.currencies.requests

import com.example.currencies.BuildConfig
import com.example.currencies.R
import com.example.currencies.utils.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object ExchangeRates {
    val BASE_URL = "https://v6.exchangerate-api.com/v6/"
    private var supportedCodes: Map<String, Pair<String, Int>> = emptyMap()


    suspend fun requestCurrencyCodes(context: android.content.Context) {
        withContext(Dispatchers.IO) {
            println("Requesting currency codes...")
            val uri = "$BASE_URL${BuildConfig.API_KEY}/codes"
            val url = try {
                URL(uri)
            } catch (e: Exception) {
                println("Error creating URL: ${e.message}")
                return@withContext
            }
            val connection = try {
                url.openConnection() as HttpURLConnection
            } catch (e: Exception) {
                println("Error opening connection: ${e.message}")
                return@withContext
            }
            try {
                connection.requestMethod = "GET"
                connection.connect()
                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    println("Error response code: $responseCode")
                    val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    println("Error Response: $errorResponse")
                    return@withContext
                }
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = org.json.JSONObject(response)
                if (jsonResponse.getString("result") == "success") {
                    val supportedCodesArray = jsonResponse.getJSONArray("supported_codes")
                    val codesMap = mutableMapOf<String, Pair<String, Int>>()
                    val countryCodes = CSVReader.getData()
                    for (i in 0 until supportedCodesArray.length()) {
                        val codePair = supportedCodesArray.getJSONArray(i)
                        val code = codePair.getString(0)
                        val name = codePair.getString(1)
                        val countryCode = CSVReader.getData()[code]
                        val countryFlag = context.resources.getIdentifier(
                            "${countryCode?.lowercase()}",
                            "drawable",
                            context.packageName
                        )
                        codesMap[code] = Pair(name, countryFlag)
                    }
                    supportedCodes = codesMap
                } else {
                    throw Exception("Failed to fetch currency codes: ${jsonResponse.getString("error-type")}")
                }
            } catch (e: Exception) {
                println("Error fetching currency codes: ${e.message}")
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun getCurrencyCodes(): Map<String, Pair<String, Int>> {
        while (supportedCodes.isEmpty()) {
            delay(1000)
        }
        return supportedCodes
    }

}
