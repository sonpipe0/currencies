package com.example.currencies.utils

import com.example.currencies.R

class CurrencyMapper {
    private val currencyData = mapOf(
        "ZAR" to Quadruple("South African Rand", "Africa", "South Africa", R.drawable.south_africa),
        "USD" to Quadruple("American Dollar", "America", "United States", R.drawable.usa),
        "BRL" to Quadruple("Brazilian Real", "America", "Brazil", R.drawable.brazil),
        "CAD" to Quadruple("Canadian Dollar", "America", "Canada", R.drawable.canada),
        "MXN" to Quadruple("Mexican Peso", "America", "Mexico", R.drawable.mexico),
        "JPY" to Quadruple("Japanese Yen", "Asia", "Japan", R.drawable.japan),
        "CNY" to Quadruple("Chinese Yuan", "Asia", "China", R.drawable.china),
        "HKD" to Quadruple("Hong Kong Dollar", "Asia", "Hong Kong", R.drawable.hong_kong),
        "IDR" to Quadruple("Indonesian Rupiah", "Asia", "Indonesia", R.drawable.indonesia),
        "ILS" to Quadruple("Israeli New Shekel", "Asia", "Israel", R.drawable.israel),
        "INR" to Quadruple("Indian Rupee", "Asia", "India", R.drawable.india),
        "KRW" to Quadruple("South Korean Won", "Asia", "South Korea", R.drawable.south_korea),
        "MYR" to Quadruple("Malaysian Ringgit", "Asia", "Malaysia", R.drawable.malaysia),
        "PHP" to Quadruple("Philippine Peso", "Asia", "Philippines", R.drawable.phillipines),
        "SGD" to Quadruple("Singapore Dollar", "Asia", "Singapore", R.drawable.singapore),
        "THB" to Quadruple("Thai Baht", "Asia", "Thailand", R.drawable.thailand),
        "TRY" to Quadruple("Turkish Lira", "Asia", "Turkey", R.drawable.turkey),
        "PLN" to Quadruple("Polish Zloty", "Eastern Europe", "Poland", R.drawable.poland),
        "BGN" to Quadruple("Bulgarian Lev", "Eastern Europe", "Bulgaria", R.drawable.bulgaria),
        "CZK" to Quadruple("Czech Koruna", "Eastern Europe", "Czech Republic", R.drawable.czech_republic),
        "HUF" to Quadruple("Hungarian Forint", "Eastern Europe", "Hungary", R.drawable.hungaria),
        "RON" to Quadruple("Romanian Leu", "Eastern Europe", "Romania", R.drawable.romania),
        "EUR" to Quadruple("Euro", "Western Europe", "European Union", R.drawable.european_union),
        "CHF" to Quadruple("Swiss Franc", "Western Europe", "Switzerland", R.drawable.switzerland),
        "DKK" to Quadruple("Danish Krone", "Western Europe", "Denmark", R.drawable.denmark),
        "GBP" to Quadruple("British Pound Sterling", "Western Europe", "Great Britain", R.drawable.great_britain),
        "ISK" to Quadruple("Icelandic Krona", "Western Europe", "Iceland", R.drawable.iceland),
        "NOK" to Quadruple("Norwegian Krone", "Western Europe", "Norway", R.drawable.norway),
        "SEK" to Quadruple("Swedish Krona", "Western Europe", "Sweden", R.drawable.sweden),
        "AUD" to Quadruple("Australian Dollar", "Oceania", "Australia", R.drawable.australia),
        "NZD" to Quadruple("New Zealand Dollar", "Oceania", "New Zealand", R.drawable.new_zealand)
    )

    fun getCurrencyInfo(code: String): Quadruple<String, String, String, Int>? {
        return currencyData[code]
    }

    fun getCurrencies(): Map<String, Triple<String, String, String>> {
        return currencyData.mapValues { Triple(it.value.first, it.value.second, it.value.third) }
    }

    fun getCountries(): Map<String, List<String>> {
        return currencyData.values.groupBy { it.second }.mapValues { it.value.map { quadruple -> quadruple.third } }
    }

    fun getDrawableResId(code: String): Int? {
        return currencyData[code]?.fourth
    }

    fun getContinents(): List<String> {
        return currencyData.values.map { it.second }.distinct()
    }

    fun getCodesAndFlagsForContinent(continent: String): List<Pair<String, Int>> {
        return currencyData.filter { it.value.second == continent }.map {
            Pair(it.key, it.value.fourth)
        }
    }

    fun getItemsForContinent(continent: String): List<String> {
        return currencyData.filter { it.value.second == continent }.map { it.value.third }
    }

    fun getCurrencyData(): Map<String, Quadruple<String, String, String, Int>> {
        return currencyData
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
