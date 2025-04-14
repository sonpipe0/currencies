package com.example.currencies.search.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.search_filter.ActionFilter
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType
import com.example.currencies.viewmodels.SelectedFilterViewModels
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModalBottomSheet(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val filterViewModel = hiltViewModel<SelectedFilterViewModels>()
    val filters by filterViewModel.filters.collectAsState()
    val currencyMode by filterViewModel.currencyMode.collectAsState()
    val onFilterClick: (Filter) -> Unit = { filter ->
        if (filters.any { it == filter }) {
            filterViewModel.removeFilter(filter)
        } else {
            filterViewModel.addFilter(filter)
        }
    }
    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState,
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
                    val mid = continents.size / 2
                    val firstHalf = continents.subList(0, mid)
                    val secondHalf = continents.subList(mid, continents.size)
                    FilterRow(filters, firstHalf) {continent ->
                        val filter = Filter(FilterType.CONTINENT, continent)
                        onFilterClick(filter)
                    }
                    FilterRow(filters, secondHalf) {continent ->
                        val filter = Filter(FilterType.CONTINENT, continent)
                        onFilterClick(filter)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.12f)
                    )
                    Text("Mkt Value", style = MaterialTheme.typography.titleMedium)
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth()
                    ) {
                        SegmentedButton(
                            selected = currencyMode == CurrencyMode.MORE_THAN,
                            onClick = {
                                filterViewModel.changeCurrencyMode(CurrencyMode.MORE_THAN)
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = 0,
                                count = 2,
                            ),
                        ) {
                            Text("More than")
                        }
                        SegmentedButton(
                            selected = currencyMode == CurrencyMode.LESS_THAN,
                            onClick = {
                                filterViewModel.changeCurrencyMode(CurrencyMode.LESS_THAN)
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = 1,
                                count = 2,
                            ),
                        ) {
                            Text("Less than")
                        }

                    }

                    val acceptedValues: List<String> = listOf(50, 100, 200, 500, 1000).map { it.toString() }
                    FilterRow(filters, acceptedValues) {
                        value ->
                        val filter = Filter(FilterType.CURRENCY, value)
                        if (filters.any { it == filter }) {
                            filterViewModel.removeFilter(filter)
                        } else {
                            filterViewModel.addFilter(filter)
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun FilterRow(selectedFilters:List<Filter>, filters:List<String>, onClick: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
    ) {
        for (filter in  filters) {
            ActionFilter(
                label = { Text(filter) },
                onClick = { onClick(filter) },
                selected = selectedFilters.any { it.value == filter },
                enabled = true
            )
        }
    }

}