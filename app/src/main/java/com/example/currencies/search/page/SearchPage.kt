package com.example.currencies.search.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.currencies.search.components.FilterChip
import com.example.currencies.search.components.ScrollableCurrencyList
import com.example.currencies.search.components.SearchBar
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.hooks.MockCurrencyRateSearcher


@Composable
fun SearchPage(hideKeyBoard: MutableState<Boolean>) {
    val currencyRateSearcher: CurrencyRateSearcher = MockCurrencyRateSearcher();
    val text: MutableState<String> = remember { mutableStateOf("") }
    return Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val focusManager: FocusManager = LocalFocusManager.current
            SearchBar(text = text, focusManager, hideKeyBoard = hideKeyBoard)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    color = Color(0xffff9704),
                    textColor = Color.Black,
                    filter = ">30 USD",
                    onClick = {})
                FilterChip(
                    color = Color(0xff0d04ff),
                    textColor = Color.White,
                    filter = "East Europe",
                    onClick = {})
                FilterChip(
                    color = Color(0xff6d6d6d),
                    textColor = Color.White,
                    isRounded = true,
                    filter = "+",
                    onClick = {})
            }
        }
        ScrollableCurrencyList(text, currencyRateSearcher)
    }
}