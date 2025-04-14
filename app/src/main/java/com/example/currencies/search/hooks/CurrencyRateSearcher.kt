package com.example.currencies.search.hooks

import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter

interface CurrencyRateSearcher {
    suspend fun searchCurrencyRateByPrefixAndFilters(
        prefix: String,
        filters: List<Filter>,
        selectedMode: CurrencyMode,
        baseCurrencyCode: String = "USD"
    ): List<CurrencyRate>;
}