package com.example.currencies.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.currencies.R
import com.example.currencies.database.CurrencyDatabase
import com.example.currencies.database.FavoriteCurrency
import com.example.currencies.requests.ApiServiceImpl
import com.example.currencies.requests.currencyCodes
import com.example.currencies.responses.LatestRatesResponse
import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType
import com.example.currencies.types.DayScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AllCurrenciesValuesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {

    private val database = CurrencyDatabase.getDatabase(context)
    private val favoriteCurrencyDao = database.favoriteCurrencyDao()

    val favoriteCurrencies = favoriteCurrencyDao.getAllFavorites().asFlow()

    private val _currencies = MutableStateFlow<List<CurrencyRate>>(emptyList())
    val currencies = _currencies.asStateFlow()

    private val _dayScheme = MutableStateFlow(DayScheme.DAILY)
    val dayScheme = _dayScheme.asStateFlow()

    private val _staticCurrencies = MutableStateFlow<List<CurrencyRate>>(emptyList())
    private val _oldCurrencies = MutableStateFlow<Map<String, Double>>(emptyMap())

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeDayScheme(dayScheme: DayScheme) {
        viewModelScope.launch {
            _dayScheme.emit(dayScheme)
            getHistoricCurrencies("USD")
        }
    }

    private fun getHistoricCurrencies(base: String) {
        changeLoading(true) // Set loading state first

        apiServiceImpl.getLastDayWeekOrMonthRates(
            context = context,
            base = base,
            scheme = _dayScheme.value,
            onSuccess = { historicResponse: LatestRatesResponse ->

                viewModelScope.launch {
                    _oldCurrencies.emit(historicResponse.conversionRates)
                    _staticCurrencies.emit(
                        _staticCurrencies.value.map {
                            val oldValue = _oldCurrencies.value[it.code]
                            val newValue = it.value
                            val dailyChangeValue = newValue - (oldValue ?: 0.0)
                            val dailyChangePercentage = (dailyChangeValue / (oldValue ?: 1.0)) * 100
                            it.copy(
                                dailyChangePercentage = dailyChangePercentage,
                                dailyChangeValue = dailyChangeValue
                            )
                        }
                    )
                    _currencies.emit(
                        _staticCurrencies.value
                    )
                    _filters.emit(
                        listOf()
                    )

                    changeLoading(false)
                }
            },
            onFail = {
                viewModelScope.launch {
                    _showRetry.value = true
                    changeLoading(false)
                }
            },
            loadingFinished = {
                // Don't set loading finished here - it's handled after data processing
            }
        )
    }

    @SuppressLint("DefaultLocale")
    private fun fetchAllCurrencies(base: String) {
        val isFirstRun = _staticCurrencies.value.isEmpty()
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
                                value = String.format(Locale.US, "%.2f", value).toDouble(),
                                dailyChangePercentage = 1.0, //this change will be used when we have memory: TODO()
                                dailyChangeValue = 1.0,      //this change will be used when we have memory: TODO()
                                continent = continent!!
                            )
                        }
                    )
                    if(isFirstRun) changeDayScheme(DayScheme.DAILY)
                }
                _staticCurrencies.value = _currencies.value
                _showRetry.value = false
                _isSearching.value = false
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

    private var filterJob: Job? = null
    fun filterCurrencies(prefix: String) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            changeLoading(true)
            delay(400)
            withContext(Dispatchers.IO) {
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
                                    val continentKey = getContinentKeyFromDisplayName(filter.value)
                                    currencyRate.continent == continentKey
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
            changeLoading(false)
        }
    }

    private fun changeLoading(boolean: Boolean) {
        if (_isSearching.value) return
        viewModelScope.launch {
            _loading.emit(boolean)
        }
    }


    fun getContinentKeyFromDisplayName(displayName: String): String {
        val res = context.resources
        return when (displayName) {
            res.getString(R.string.africa) -> "Africa"
            res.getString(R.string.asia) -> "Asia"
            res.getString(R.string.western_europe) -> "Western Europe"
            res.getString(R.string.eastern_europe) -> "Eastern Europe"
            res.getString(R.string.oceania) -> "Oceania"
            res.getString(R.string.america) -> "America"
            else -> displayName
        }
    }

    fun getKeyFromContinentKey(continentKey: String): String {
        return when (continentKey) {
            "Africa" -> context.getString(R.string.africa)
            "Asia" -> context.getString(R.string.asia)
            "Western Europe" -> context.getString(R.string.western_europe)
            "Eastern Europe" -> context.getString(R.string.eastern_europe)
            "Oceania" -> context.getString(R.string.oceania)
            "America" -> context.getString(R.string.america)
            else -> continentKey
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

    /**
     * Toggle favorite status of a currency
     * Also updates the currency with the latest fetched values
     * @param currencyRate The currency to toggle favorite status
     */
    fun toggleFavorite(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            // Find the most up-to-date values for this currency from the fetched currencies
            val updatedCurrency = _staticCurrencies.value.find { it.code == currencyRate.code } ?: currencyRate

            // Check if currency is already a favorite
            val isFavorite = favoriteCurrencyDao.isFavorite(updatedCurrency.code)

            if (isFavorite) {
                // Remove from favorites
                favoriteCurrencyDao.deleteFavoriteByCode(updatedCurrency.code)
            } else {
                // Add to favorites with latest values
                val favoriteCurrency = FavoriteCurrency(
                    code = updatedCurrency.code,
                    name = updatedCurrency.name,
                    continent = updatedCurrency.continent,
                    value = updatedCurrency.value,
                    dailyChangePercentage = updatedCurrency.dailyChangePercentage
                )
                favoriteCurrencyDao.insertFavorite(favoriteCurrency)
            }
        }
    }

    /**
     * Check if a currency is a favorite
     */
    suspend fun isFavorite(currencyCode: String): Boolean {
        return favoriteCurrencyDao.isFavorite(currencyCode)
    }

    /**
     * Updates the values of favorite currencies with the latest fetched data
     * Call this when favorite currencies are loaded or when new data is fetched
     */
    fun updateFavoriteCurrenciesValues() {
        viewModelScope.launch {
            // Get the current list of favorite currencies
            val favorites = favoriteCurrencyDao.getAllFavorites().value ?: emptyList()

            if (favorites.isNotEmpty() && _staticCurrencies.value.isNotEmpty()) {
                // For each favorite, find its corresponding up-to-date data in staticCurrencies
                favorites.forEach { favoriteCurrency ->
                    val currentData = _staticCurrencies.value.find { it.code == favoriteCurrency.code }

                    if (currentData != null) {
                        // Update the favorite with current values
                        favoriteCurrencyDao.updateFavoriteCurrencyValues(
                            favoriteCurrency.code,
                            currentData.value,
                            currentData.dailyChangePercentage
                        )
                    }
                }
            }
        }
    }
}
