package com.example.currencies.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.currencies.pages.BiometricLoginPage
import com.example.currencies.pages.FavoritesPage
import com.example.currencies.pages.GoogleLoginPage
import com.example.currencies.pages.SearchPage
import com.example.currencies.pages.SwapPage
import com.example.currencies.pages.UserConfigPage
import com.example.currencies.viewmodels.AuthViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavHostComposable(hideKeyboard: MutableState<Boolean>, innerPadding: PaddingValues, navController: NavHostController) {
    // Use AuthViewModel to check authentication status
    val authViewModel = hiltViewModel<AuthViewModel>()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isBiometricAuthenticated by authViewModel.isBiometricAuthenticated.collectAsState()

    // Determine start destination based on auth state
    val startDestination = when {
        !isLoggedIn -> NavigationDestination.GOOGLE_LOGIN
        !isBiometricAuthenticated -> NavigationDestination.BIOMETRIC_LOGIN
        else -> Screens.SEARCH.name
    }

    return NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        // Google Login Screen
        composable(route = NavigationDestination.GOOGLE_LOGIN) {
            GoogleLoginPage(
                onGoogleLoginSuccess = {
                    navController.navigate(NavigationDestination.BIOMETRIC_LOGIN) {
                        popUpTo(NavigationDestination.GOOGLE_LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Biometric Login Screen
        composable(route = NavigationDestination.BIOMETRIC_LOGIN) {
            BiometricLoginPage(
                onAuthSuccess = {
                    authViewModel.setBiometricAuthenticated(true)
                    navController.navigate(Screens.SEARCH.name) {
                        popUpTo(NavigationDestination.BIOMETRIC_LOGIN) { inclusive = true }
                    }
                },
                onSignOut = {
                    navController.navigate(NavigationDestination.GOOGLE_LOGIN) {
                        popUpTo(NavigationDestination.BIOMETRIC_LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Existing screens
        composable(route = Screens.SEARCH.name) {
            SearchPage(hideKeyBoard = hideKeyboard)
        }

        composable(route = Screens.SWAP.name) {
            SwapPage(hideKeyBoard = hideKeyboard)
        }

        composable(route = Screens.STARRED.name) {
            FavoritesPage(hideKeyBoard = hideKeyboard)
        }

        composable(route = Screens.USER_CONFIG.name) {
            UserConfigPage(
                navController = navController,
                hideKeyBoard = hideKeyboard
            )
        }
    }
}
