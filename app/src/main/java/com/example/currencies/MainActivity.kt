package com.example.currencies

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.compose.CurrenciesTheme
import com.example.currencies.navigation.BottomNavigationBar
import com.example.currencies.navigation.NavHostComposable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val hideKeyBoard = remember { mutableStateOf(false) }
            val navController = rememberNavController()
            val context = LocalContext.current
            CurrenciesTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { hideKeyBoard.value = true },
                    bottomBar = {
                        BottomNavigationBar { dest ->
                            if (dest != navController.currentDestination?.route) {
                                navController.navigate(dest)
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background,
                ) { innerPadding ->
                    NavHostComposable(hideKeyBoard, innerPadding, navController)
                }
            }
        }
    }
}
