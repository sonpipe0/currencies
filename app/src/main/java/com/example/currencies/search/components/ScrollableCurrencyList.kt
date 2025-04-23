package com.example.currencies.search.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.viewmodels.AllCurrenciesValuesViewModel
import dropShadow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScrollableCurrencyList(
    prefix: MutableState<String>,
) {
    val allCurrenciesValuesViewModels = hiltViewModel<AllCurrenciesValuesViewModel>()
    val currencyRates by allCurrenciesValuesViewModels.currencies.collectAsState()
    val isLoading by allCurrenciesValuesViewModels.loading.collectAsState()
    val filters by allCurrenciesValuesViewModels.filters.collectAsState()
    val currencyMode by allCurrenciesValuesViewModels.currencyMode.collectAsState()
    val showRetry by allCurrenciesValuesViewModels.showRetry.collectAsState()

    LaunchedEffect(prefix.value, filters, currencyMode) {
        allCurrenciesValuesViewModels.filterCurrencies(
            prefix = prefix.value,
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if(showRetry){
        Box(
            modifier = Modifier.fillMaxSize().
            clickable {
                allCurrenciesValuesViewModels.retry(base = "USD")
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NewReleases,
                contentDescription = "reload"
            )
        }
    } else {
        Column(
            modifier = Modifier
                .dropShadow(
                    shape = RectangleShape,
                    color = Color.Black.copy(0.10f),
                    blur = 4.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            currencyRates.forEachIndexed { index, currencyRate ->
                CurrencyRateCard(
                    currencyRate = currencyRate,
                    last = index == currencyRates.size - 1,
                    onFavorite = {},
                )
            }
        }
    }
}

