package com.example.currencies.search.components

import DayScheme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.search_filter.ActionFilter
import com.example.currencies.search_filter.CurrencyMode
import com.example.currencies.search_filter.Filter
import com.example.currencies.search_filter.FilterType
import com.example.currencies.ui.theme.Padding
import com.example.currencies.viewmodels.AllCurrenciesValuesViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModalBottomSheet(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val filterViewModel = hiltViewModel<AllCurrenciesValuesViewModel>()
    val filters by filterViewModel.filters.collectAsState()
    val currencyMode by filterViewModel.currencyMode.collectAsState()
    val onFilterClick: (Filter) -> Unit = { filter ->
        if (filters.any { it == filter }) {
            filterViewModel.removeFilter(filter)
        } else {
            filterViewModel.addFilter(filter)
        }
    }
    val selectedOption by filterViewModel.dayScheme.collectAsState()
    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState,
            content = {
                Column(
                    modifier = Modifier.padding(Padding.large),
                    verticalArrangement = Arrangement.spacedBy(Padding.medium)
                ) {
                    Text("Compare by", style = MaterialTheme.typography.titleLarge)
                    var isMenuOpen by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Select the comparison Scheme",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                        )

                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(1f),
                            expanded = isMenuOpen,
                            onExpandedChange = { isMenuOpen = !isMenuOpen }
                        ) {
                            // Anchor element
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                readOnly = true,
                                value = selectedOption.name.lowercase(),
                                onValueChange = {},
                                label = { Text("Timeframe") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMenuOpen)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            // Dropdown menu
                            ExposedDropdownMenu(
                                expanded = isMenuOpen,
                                onDismissRequest = { isMenuOpen = false }
                            ) {
                                DayScheme.entries.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.name.lowercase()) },
                                        onClick = {
                                            filterViewModel.changeDayScheme(option)
                                            isMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Padding.small),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.12f)
                    )
                    Text("Filter by", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Select the currencies you want to see",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Padding.small),
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
                        modifier = Modifier.padding(vertical = Padding.small),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.12f)
                    )
                    Text("Mkt Value", style = MaterialTheme.typography.titleMedium)
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.padding(horizontal = Padding.main).fillMaxWidth()
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
        horizontalArrangement = Arrangement.spacedBy(Padding.medium),
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