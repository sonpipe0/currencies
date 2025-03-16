package com.example.currencies.search.hooks

import com.example.currencies.search.types.CurrencyRate

interface CurrencyRateSearcher {
    suspend fun searchCurrencyRateByPrefix(prefix: String, baseCurrencyCode: String = "USD"): List<CurrencyRate>;
}