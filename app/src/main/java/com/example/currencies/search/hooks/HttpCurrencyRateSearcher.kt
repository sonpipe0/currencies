package com.example.currencies.search.hooks

import com.example.currencies.search.types.CurrencyRate

class HttpCurrencyRateSearcher: CurrencyRateSearcher {

    override suspend fun searchCurrencyRateByPrefix(
        prefix: String,
        baseCurrencyCode: String
    ): List<CurrencyRate> {
        TODO("Not yet implemented")
    }
}