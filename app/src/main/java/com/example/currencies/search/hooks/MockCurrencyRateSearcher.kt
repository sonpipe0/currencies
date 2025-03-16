package com.example.currencies.search.hooks

import com.example.currencies.search.types.CurrencyRate
import kotlinx.coroutines.delay

class MockCurrencyRateSearcher: CurrencyRateSearcher {
    override suspend fun searchCurrencyRateByPrefix(
        prefix: String,
        baseCurrencyCode: String
    ): List<CurrencyRate> {
        delay(500);
        return listOf(
            CurrencyRate(
                name = "Euro",
                code = "EUR",
                value = 0.85,
                dailyChangePercentage = 0.1,
                dailyChangeValue = 0.001
            ),
            CurrencyRate(
                name = "British Pound",
                code = "GBP",
                value = 0.75,
                dailyChangePercentage = -0.05,
                dailyChangeValue = -0.0005
            ),
            CurrencyRate(
                name = "British Pound",
                code = "GBP",
                value = 0.75,
                dailyChangePercentage = -0.05,
                dailyChangeValue = -0.0005
            ),
            CurrencyRate(
                name = "Japanese Yen",
                code = "JPY",
                value = 110.0,
                dailyChangePercentage = 0.2,
                dailyChangeValue = 0.22
            ),
            CurrencyRate(
                name = "Swiss Franc",
                code = "CHF",
                value = 0.92,
                dailyChangePercentage = 0.15,
                dailyChangeValue = 0.0014
            ),
            CurrencyRate(
                name = "Canadian Dollar",
                code = "CAD",
                value = 1.25,
                dailyChangePercentage = -0.1,
                dailyChangeValue = -0.0012
            ),
            CurrencyRate(
                name = "Australian Dollar",
                code = "AUD",
                value = 1.35,
                dailyChangePercentage = 0.05,
                dailyChangeValue = 0.0007
            ),
            CurrencyRate(
                name = "Chinese Yuan",
                code = "CNY",
                value = 6.45,
                dailyChangePercentage = 0.1,
                dailyChangeValue = 0.0065
            ),
            CurrencyRate(
                name = "Indian Rupee",
                code = "INR",
                value = 74.0,
                dailyChangePercentage = -0.2,
                dailyChangeValue = -0.148
            ),
            CurrencyRate(
                name = "Argentine Peso",
                code = "ARS",
                value = 98.0,
                dailyChangePercentage = -0.3,
                dailyChangeValue = -0.294
            )
        )
    }
}
