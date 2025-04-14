package com.example.currencies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedFilterViewModels @Inject constructor(): ViewModel() {
    private var _filters = MutableStateFlow(listOf<Filter>())
    private var _currencyMode = MutableStateFlow(CurrencyMode.MORE_THAN)
    val currencyMode = _currencyMode.asStateFlow()
    val filters = _filters.asStateFlow()


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