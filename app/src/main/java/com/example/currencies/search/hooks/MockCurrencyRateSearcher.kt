package com.example.currencies.search.hooks

import com.example.currencies.requests.currencyCodes
import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType

class MockCurrencyRateSearcher: CurrencyRateSearcher {
    override suspend fun searchCurrencyRateByPrefixAndFilters(
        prefix: String,
        filters: List<Filter>,
        selectedMode: CurrencyMode,
        baseCurrencyCode: String
    ): List<CurrencyRate> {
        return currencyCodes.map {
            CurrencyRate(
                code = it.key,
                name = it.value.second,
                value = 100.0,
                dailyChangePercentage = 5.0,
                dailyChangeValue = 20.0,
                continent = it.value.first
            )
        }.filter {
            it.name.startsWith(prefix, ignoreCase = true) || it.code.startsWith(prefix, ignoreCase = true)
        }.filter { currencyRate ->
            filters.all { filter ->
                when (filter.type) {
                    FilterType.CONTINENT -> {
                        filter.value == currencyRate.continent
                    }
                    FilterType.CURRENCY -> {
                        when (selectedMode) {
                            CurrencyMode.MORE_THAN -> {
                                currencyRate.value > filter.value.toDouble()
                            }
                            CurrencyMode.LESS_THAN -> {
                                currencyRate.value < filter.value.toDouble()
                            }
                        }
                    }
                }
            }
        }
    }
}
