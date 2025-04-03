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
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.types.CurrencyRate
import dropShadow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            currencyRates.value = currencyRateSearcher.searchCurrencyRateByPrefix(prefix.value, "USD").take(3)
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
                        last = index == currencyRates.value.size - 1
                    )
                }
            }
        }
    }

