package com.example.currencies.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencies.requests.ApiServiceImpl
import com.example.currencies.requests.currencyCodes
import com.example.currencies.responses.LatestRatesResponse
import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllCurrenciesValuesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {
    private val _currencies = MutableStateFlow<List<CurrencyRate>>(emptyList())
    val currencies = _currencies.asStateFlow()

    private val _staticCurrencies = MutableStateFlow<List<CurrencyRate>>(emptyList())

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    private var _filters = MutableStateFlow(listOf<Filter>())
    private var _currencyMode = MutableStateFlow(CurrencyMode.MORE_THAN)

    private var _isSearching = MutableStateFlow<Boolean>(false)

    val currencyMode = _currencyMode.asStateFlow()
    val filters = _filters.asStateFlow()

    init {
        fetchAllCurrencies("USD")
    }

    fun retry(base: String) {
        changeLoading(true)
        fetchAllCurrencies(base)
        changeLoading(false)
    }

    @SuppressLint("DefaultLocale")
    public fun fetchAllCurrencies(base: String) {
        if (_staticCurrencies.value.isNotEmpty()) return
        viewModelScope.launch {
            _isSearching.emit(true)
        }
        apiServiceImpl.getLatestRates(
            base = base.uppercase(),
            context = context,
            onSuccess = { latestRatesResponse: LatestRatesResponse ->
                viewModelScope.launch {
                    _currencies.emit(
                        latestRatesResponse.conversionRates.map { (key, value) ->
                            val name = currencyCodes[key]?.second ?: key
                            val continent = currencyCodes[key]?.first ?: "Unknown"
                            CurrencyRate(
                                name = name!!,
                                code = key,
                                value = String.format("%.2f", value).toDouble(),
                                dailyChangePercentage = 1.0, //this change will be used when we have memory: TODO()
                                dailyChangeValue = 1.0,      //this change will be used when we have memory: TODO()
                                continent = continent!!
                            )
                        }
                    )
                }
                _showRetry.value = false
                _isSearching.value = false
                _staticCurrencies.value = _currencies.value
            },
            onFail = {
                _showRetry.value = true
                _isSearching.value = false
            },
            loadingFinished = {
                _loading.value = false
                _isSearching.value = false
            }
        )
    }

    fun filterCurrencies(prefix: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                changeLoading(true)
                delay(400)
                _currencies.emit(
                    _staticCurrencies.value.filter {
                        it.name.startsWith(prefix, ignoreCase = true) || it.code.startsWith(
                            prefix,
                            ignoreCase = true
                        )
                    }.filter { currencyRate ->
                        _filters.value.all { filter ->
                            when (filter.type) {
                                FilterType.CONTINENT -> {
                                    filter.value == currencyRate.continent
                                }

                                FilterType.CURRENCY -> {
                                    when (_currencyMode.value) {
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
                )
            }
        }.invokeOnCompletion {
            changeLoading(false)
        }
    }

    private fun changeLoading(boolean: Boolean) {
        println(_isSearching.value)
        if (_isSearching.value) return
        viewModelScope.launch {
            _loading.emit(boolean)
        }
    }

    /**
     * Adds a filter to the list of filters.
     *
     * if [filters] has a filter of the same type, it will be replaced.
     *
     * if [filter] has the same value as an existing filter, it won't be added.
     *
     * @param filter  The filter to be added.
     */
    fun addFilter(filter: Filter) {
        if (_filters.value.any { it == filter }) {
            return
        }
        val existingFilter = _filters.value.find { it.type == filter.type }
        viewModelScope.launch {
            if (existingFilter != null) {
                _filters.emit(_filters.value.filter { it != existingFilter } + filter)
            } else {
                _filters.emit(_filters.value + filter)
            }
        }
    }

    /**
     * Removes a filter from the list of filters.
     *
     * if [filter] is not in the list, it will not be removed.
     *
     * @param filter  The filter to be removed.
     */
    fun removeFilter(filter: Filter) {
        if (_filters.value.any { it == filter }) {
            viewModelScope.launch {
                _filters.emit(_filters.value - filter)
            }
        }
    }

    /**
     * Sets the selected currency mode.
     *
     * @param mode  The currency mode to be set.
     */
    fun changeCurrencyMode(mode: CurrencyMode) {
        viewModelScope.launch {
            _currencyMode.emit(mode)
        }
    }
}