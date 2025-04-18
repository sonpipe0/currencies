package com.example.currencies.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.search.components.FilterModalBottomSheet
import com.example.currencies.search.components.ScrollableCurrencyList
import com.example.currencies.search.components.SearchBar
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.hooks.MockCurrencyRateSearcher
import com.example.currencies.search_filter.ActionFilter
import com.example.currencies.search_filter.FilterType
import com.example.currencies.viewmodels.AllCurrenciesValuesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(hideKeyBoard: MutableState<Boolean>) {
    val currencyRateSearcher: CurrencyRateSearcher = MockCurrencyRateSearcher();
    val text: MutableState<String> = remember { mutableStateOf("") }
    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
    val filterViewModel = hiltViewModel<AllCurrenciesValuesViewModel>()
    val filters by filterViewModel.filters.collectAsState()
    val currencyMode by filterViewModel.currencyMode.collectAsState()
    return Column(
        modifier = Modifier.padding(top = 16.dp),
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
                OpenFiltersButton { isExpanded.value = true }
                filters.sortedBy { it.type }.forEach() { filter ->
                    ActionFilter(
                        onClick = {
                            filterViewModel.removeFilter(filter)
                        },
                        label = { Text(
                            if (filter.type == FilterType.CURRENCY) {
                                "${filter.value} $currencyMode"
                            } else {
                                filter.value
                            }
                        ) },
                        selected = true,
                        enabled = true
                    )
                }
            }
        }
        ScrollableCurrencyList(text, currencyRateSearcher)

        FilterModalBottomSheet(isExpanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        )

    }
}


@Composable
fun OpenFiltersButton(onClick: () -> Unit) {

    Box (
        content = {
            Icon(
                imageVector = Icons.Default.FilterList,
                modifier = Modifier.size(20.dp),
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp)
            .padding(8.dp)
    )
}