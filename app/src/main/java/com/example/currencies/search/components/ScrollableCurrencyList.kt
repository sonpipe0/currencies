package com.example.currencies.search.components

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.types.CurrencyRate
import dropShadow

@Composable
fun ScrollableCurrencyList(prefix: MutableState<String>, currencyRateSearcher: CurrencyRateSearcher) {
    val currencyRates = remember { mutableStateOf(emptyList<CurrencyRate>()) }
    val isLoading = remember { mutableStateOf(false) }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(prefix.value) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(300) // Debounce time
            println("Searching")
            isLoading.value = true
            currencyRates.value = currencyRateSearcher.searchCurrencyRateByPrefix(prefix.value, "USD")
            isLoading.value = false
        }
    }

    if(isLoading.value){
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(top = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() //change to skeletons
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .dropShadow(
                    color = Color.Black.copy(0.10f),
                    shape = RectangleShape,
                    blur = 4.dp,
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                currencyRates.value.forEachIndexed { index, currencyRate ->
                    CurrencyRateCard(
                        currencyRate = currencyRate,
                        last = index == currencyRates.value.size - 1
                    )
                }
            }
        }
    }
}
