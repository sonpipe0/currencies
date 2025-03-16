package com.example.currencies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.currencies.search.components.FilterChip
import com.example.currencies.search.components.ScrollableCurrencyList
import com.example.currencies.search.components.SearchBar
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.hooks.MockCurrencyRateSearcher
import com.example.currencies.ui.theme.CurrenciesTheme

class MainActivity : ComponentActivity() {
    val currencyRateSearcher: CurrencyRateSearcher = MockCurrencyRateSearcher();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrenciesTheme {
                val hideKeyBoard = remember { mutableStateOf(false) }
                Scaffold(modifier = Modifier.fillMaxSize().clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { hideKeyBoard.value = true},
                    containerColor = Color(0xffEDEDED)) { innerPadding ->
                    val text: MutableState<String> = remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            SearchBar(text = text, hideKeyBoard = hideKeyBoard)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                FilterChip(color = Color(0xffff9704), textColor = Color.Black, filter = ">30 USD", onClick = {})
                                FilterChip(color = Color(0xff0d04ff), textColor = Color.White ,filter = "East Europe", onClick = {})
                                FilterChip(color = Color(0xff6d6d6d), textColor = Color.White, isRounded = true ,filter = "+", onClick = {})
                            }
                        }
                        ScrollableCurrencyList(text, currencyRateSearcher)
                    }
                }
            }
        }
    }
}
