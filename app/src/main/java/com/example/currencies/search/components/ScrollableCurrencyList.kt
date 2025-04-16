package com.example.currencies.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.viewmodels.SelectedFilterViewModels
import dropShadow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScrollableCurrencyList(prefix: MutableState<String>, currencyRateSearcher: CurrencyRateSearcher) {
    val currencyRates = remember { mutableStateOf(emptyList<CurrencyRate>()) }
    val isLoading = remember { mutableStateOf(false) }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val filterViewModel = hiltViewModel<SelectedFilterViewModels>()
    val filters by filterViewModel.filters.collectAsState()
    val currencyMode by filterViewModel.currencyMode.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    // we are going to have to use a view model to store our favorites ...
    LaunchedEffect(prefix.value, filters, currencyMode) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            isLoading.value = true
            // perform in the IO thread because it takes time and we don't want to block the UI thread
            val results = withContext(Dispatchers.IO) {
                currencyRateSearcher.searchCurrencyRateByPrefixAndFilters(
                    prefix.value,
                    filters,
                    currencyMode,
                    "USD"
                )
            }
            currencyRates.value = results
            isLoading.value = false
        }
    }

    if(isLoading.value){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() //change to skeletons
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
                currencyRates.value.forEachIndexed { index, currencyRate ->
                    CurrencyRateCard(
                        currencyRate = currencyRate,
                        last = index == currencyRates.value.size - 1,
                        onFavorite = {},
                    )
                }
            }
        }
    }

