package com.example.currencies.utils

import com.example.currencies.CurrenciesApplication
import java.io.BufferedReader
import java.io.InputStreamReader

val csvData: Map<String, Pair<String, String>> = getData()


private fun readCSV(): Map<String, Pair<String, String>> {
    val data: MutableMap<String, Pair<String, String>> = mutableMapOf()
    val fileName = "country_codes.csv"
    val inputStream = try {
        CurrenciesApplication.getResources().assets.open(fileName)
    } catch (e: Exception) {
        e.printStackTrace()
        return data
    }
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.use { br ->
        var line: String?
        br.readLine()
        while (br.readLine().also { line = it } != null) {
            val row = line!!.split(",").map { it.trim() }
            if (row.size >= 2) {
                val country = row[0]
                val currency = row[1]
                val continent = row[2]
                data[currency] = Pair(country, continent)
            }
        }
    }
    return data
}

private fun getData(): Map<String, Pair<String, String>> {
    return readCSV()
}
