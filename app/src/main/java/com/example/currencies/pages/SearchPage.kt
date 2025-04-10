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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.currencies.search.components.ScrollableCurrencyList
import com.example.currencies.search.components.SearchBar
import com.example.currencies.search.hooks.CurrencyRateSearcher
import com.example.currencies.search.hooks.MockCurrencyRateSearcher
import com.example.currencies.search_filter.ActionFilter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(hideKeyBoard: MutableState<Boolean>) {
    val currencyRateSearcher: CurrencyRateSearcher = MockCurrencyRateSearcher();
    val text: MutableState<String> = remember { mutableStateOf("") }
    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
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
                            isExpanded.value = true
                        }
                        .padding(horizontal = 8.dp)
                        .padding(8.dp)
                )
                ActionFilter(
                    onClick = {
                    },
                    label = { Text("Western Europe") },
                    selected = true,
                    enabled = true
                )
            }
        }
        ScrollableCurrencyList(text, currencyRateSearcher)

        if (isExpanded.value) {
            ModalBottomSheet(
                onDismissRequest = { isExpanded.value = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Filter by", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Select the currencies you want to see",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(0.12f)
                        )
                        Text("Continents", style = MaterialTheme.typography.titleMedium)

                        val continents = listOf("Africa", "Asia", "Western Europe", "Eastern Europe","Oceania", "America")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (continent in continents.take(3)) {
                                ActionFilter(
                                    label = { Text(continent) },
                                    onClick = { /* Handle click */ },
                                    selected = false,
                                    enabled = true
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (continent in continents.drop(3)) {
                                ActionFilter(
                                    label = { Text(continent) },
                                    onClick = { /* Handle click */ },
                                    selected = false,
                                    enabled = true
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(0.12f)
                        )
                        Text("Mkt Value", style = MaterialTheme.typography.titleMedium)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            ActionFilter(
                                label = { Text("More Than") },
                                onClick = { /* Handle click */ },
                                selected = false,
                                enabled = true
                            )
                            ActionFilter(
                                label = { Text("Less Than") },
                                onClick = { /* Handle click */ },
                                selected = false,
                                enabled = true
                            )
                        }

                        Row (
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            val acceptedValues: List<Int> = listOf(50, 100, 200, 500, 1000)
                            for (value in acceptedValues) {
                                ActionFilter(
                                    label = { Text("$value") },
                                    onClick = { /* Handle click */ },
                                    selected = true,
                                    enabled = true,
                                    isLeadingIcon = true
                                )
                            }

                        }
                    }
                }
            )
        }

    }
}