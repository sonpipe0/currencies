package com.example.currencies.utils
class CurrencyMapper {
    fun getCurrencies(): Map<String, Pair<String, String>> {
        return mapOf(
            // Africa
            "ZAR" to ("South African Rand" to "Africa"),

            // America
            "BRL" to ("Brazilian Real" to "America"),
            "CAD" to ("Canadian Dollar" to "America"),
            "MXN" to ("Mexican Peso" to "America"),
            "USD" to ("American Dollar" to "America"),

            // Asia
            "CNY" to ("Chinese Yuan" to "Asia"),
            "HKD" to ("Hong Kong Dollar" to "Asia"),
            "IDR" to ("Indonesian Rupiah" to "Asia"),
            "ILS" to ("Israeli New Shekel" to "Asia"),
            "INR" to ("Indian Rupee" to "Asia"),
            "JPY" to ("Japanese Yen" to "Asia"),
            "KRW" to ("South Korean Won" to "Asia"),
            "MYR" to ("Malaysian Ringgit" to "Asia"),
            "PHP" to ("Philippine Peso" to "Asia"),
            "SGD" to ("Singapore Dollar" to "Asia"),
            "THB" to ("Thai Baht" to "Asia"),
            "TRY" to ("Turkish Lira" to "Asia"),

            // Eastern Europe
            "BGN" to ("Bulgarian Lev" to "Eastern Europe"),
            "CZK" to ("Czech Koruna" to "Eastern Europe"),
            "HUF" to ("Hungarian Forint" to "Eastern Europe"),
            "PLN" to ("Polish Zloty" to "Eastern Europe"),
            "RON" to ("Romanian Leu" to "Eastern Europe"),

            // Western Europe
            "CHF" to ("Swiss Franc" to "Western Europe"),
            "DKK" to ("Danish Krone" to "Western Europe"),
            "EUR" to ("Euro" to "Western Europe"),
            "GBP" to ("British Pound Sterling" to "Western Europe"),
            "ISK" to ("Icelandic Krona" to "Western Europe"),
            "NOK" to ("Norwegian Krone" to "Western Europe"),
            "SEK" to ("Swedish Krona" to "Western Europe"),

            // Oceania
            "AUD" to ("Australian Dollar" to "Oceania"),
            "NZD" to ("New Zealand Dollar" to "Oceania")
        )
    }
}
