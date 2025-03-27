package com.example.currencies.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.currencies.search.page.SearchPage
import com.example.currencies.swap.SwapPage

@Composable
fun NavHostComposable(hideKeyboard: MutableState<Boolean> ,innerPadding: PaddingValues, navController: NavHostController) {
    return NavHost(
        navController = navController,
        startDestination = Screens.SEARCH.name,
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(
            route = Screens.SEARCH.name,
        ) {
            SearchPage(hideKeyBoard = hideKeyboard)
        }
        composable(route = Screens.SWAP.name) {
            SwapPage(hideKeyBoard = hideKeyboard)
        }
        composable(route = Screens.STARRED.name) {
            SearchPage(hideKeyBoard = hideKeyboard)
        }
    }
}