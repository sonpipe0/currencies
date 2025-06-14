package com.example.currencies.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.currencies.viewmodels.UserViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavController,
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val user by userViewModel.userData.collectAsState()

    TopAppBar(
        title = {
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Screens.USER_CONFIG.name)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Configure User"
                )
            }
        }
    )
}
