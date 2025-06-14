package com.example.currencies.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.R
import com.example.currencies.search.components.CurrencyRateCard
import com.example.currencies.ui.theme.Padding
import com.example.currencies.viewmodels.AllCurrenciesValuesViewModel
import dropShadow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoritesPage(hideKeyBoard: MutableState<Boolean>) {
    val viewModel = hiltViewModel<AllCurrenciesValuesViewModel>()
    val favorites by viewModel.favoriteCurrencies.collectAsState(initial = emptyList())
    val isLoading by viewModel.loading.collectAsState()

    // Update favorite currencies with latest values when this page is shown
    DisposableEffect(Unit) {
        viewModel.updateFavoriteCurrenciesValues()
        onDispose { }
    }

    Column(
        modifier = Modifier.padding(top = Padding.large),
        verticalArrangement = Arrangement.spacedBy(Padding.medium),
    ) {
        Box(modifier = Modifier.padding(horizontal = Padding.main)) {
            Text(
                text = stringResource(R.string.favorite_currencies),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_favorites) + " " + stringResource(R.string.add_some_favorites),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
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
                favorites.forEach { favorite ->
                    // Use the updated toCurrencyRate method without parameters
                    val currencyRate = favorite.toCurrencyRate()
                    CurrencyRateCard(
                        currencyRate = currencyRate,
                        last = favorite == favorites.last(),
                        onFavorite = {
                            viewModel.toggleFavorite(currencyRate)
                        },
                        isFavorite = true
                    )
                }
            }
        }
    }
}

