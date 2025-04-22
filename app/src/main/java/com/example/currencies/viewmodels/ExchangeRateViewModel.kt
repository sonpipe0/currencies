package com.example.currencies.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencies.requests.ApiServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {
    private val _currentExchangeRate = MutableStateFlow<Double>(1.0)
    val currentExchangeRate = _currentExchangeRate.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _retry = MutableStateFlow(false)

    init {
        fetchExchangeRates("USD", "EUR")
    }


    fun fetchExchangeRates(base: String, target: String) {
        changeLoading(true)
        apiServiceImpl.pairConversion(
            from = base.uppercase(),
            to = target.uppercase(),
            context = context,
            onSuccess = { rate: Double ->
                viewModelScope.launch {
                    _currentExchangeRate.emit(
                        rate
                    )
                    _retry.emit(false)
                    _loading.emit(false)
                }
            },
            onFail = {
                viewModelScope.launch {
                    _retry.emit(true)
                }
            },
        )
    }

    private fun changeLoading(boolean: Boolean) {
        viewModelScope.launch {
            _loading.emit(boolean)
        }
    }
}