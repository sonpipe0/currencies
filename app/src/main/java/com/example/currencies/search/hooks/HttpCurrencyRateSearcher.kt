package com.example.currencies.search.hooks

import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter

class HttpCurrencyRateSearcher: CurrencyRateSearcher {

    override suspend fun searchCurrencyRateByPrefixAndFilters(
        prefix: String,
        filters: List<Filter>,
        selectedMode: CurrencyMode,
        baseCurrencyCode: String
    ): List<CurrencyRate> {
        TODO("Not yet implemented")
    }
}