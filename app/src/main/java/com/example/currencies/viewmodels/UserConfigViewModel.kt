package com.example.currencies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserConfigViewModel @Inject constructor() : ViewModel() {
    private val _moneyAmount = MutableStateFlow(0.0)
    val moneyAmount: StateFlow<Double> = _moneyAmount

    fun updateMoneyAmount(newAmount: Double) {
        viewModelScope.launch {
            _moneyAmount.value = newAmount
        }
    }
}
