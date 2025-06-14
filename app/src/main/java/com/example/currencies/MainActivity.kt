package com.example.currencies

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.compose.CurrenciesTheme
import com.example.currencies.navigation.BottomNavigationBar
import com.example.currencies.navigation.NavHostComposable
import com.example.currencies.navigation.NavigationDestination
import com.example.currencies.notification.notificationChannelID
import com.example.currencies.viewmodels.AuthViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        enableEdgeToEdge()
        setContent {
            CurrenciesTheme {
                MainScreen()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_description)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainScreen(){
    val hideKeyBoard = remember { mutableStateOf(false) }
    val navController = rememberNavController()

    // Get auth state to determine if we should show the NavBar
    val authViewModel = hiltViewModel<AuthViewModel>()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isBiometricAuthenticated by authViewModel.isBiometricAuthenticated.collectAsState()

    // Only show NavBar after complete authentication
    val showNavBar = isLoggedIn && isBiometricAuthenticated

    // Track current route to avoid showing NavBar on auth screens
    val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val isAuthScreen = currentRoute.value?.destination?.route == NavigationDestination.GOOGLE_LOGIN ||
                       currentRoute.value?.destination?.route == NavigationDestination.BIOMETRIC_LOGIN

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { hideKeyBoard.value = true },
        bottomBar = {
            // Only show NavBar after complete authentication
            if (showNavBar && !isAuthScreen) {
                BottomNavigationBar { dest ->
                    if (dest != navController.currentDestination?.route) {
                        navController.navigate(dest)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        NavHostComposable(hideKeyBoard, innerPadding, navController)
    }
}
