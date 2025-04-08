package com.example.currencies.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object CSVReader {
    private val data: MutableMap<String, String> = mutableMapOf()

    fun readCSV(context: Context, fileName: String) {

        val inputStream = try {
            context.assets.open(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.use { br ->
            var line: String?
            // Skip the header line
            br.readLine()
            while (br.readLine().also { line = it } != null) {
                val row = line!!.split(",").map { it.trim() }
                if (row.size >= 2) {
                    val value = row[0]
                    val key = row[1]
                    data[key] = value
                }
            }
        }
    }

    fun getData(): Map<String, String> {
        return data
    }
}
