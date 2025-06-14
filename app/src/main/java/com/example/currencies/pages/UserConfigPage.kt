package com.example.currencies.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.currencies.R
import com.example.currencies.navigation.Screens
import com.example.currencies.ui.theme.Box
import com.example.currencies.ui.theme.Padding
import com.example.currencies.viewmodels.ScheduleNotificationViewModel
import com.example.currencies.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun UserConfigPage(
    navController: NavController,
    hideKeyBoard: MutableState<Boolean>
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val scheduleNotificationViewModel = hiltViewModel<ScheduleNotificationViewModel>()
    val user by userViewModel.userData.collectAsState()
    val focusManager = LocalFocusManager.current
    val notificationsEnabled by scheduleNotificationViewModel.notificationsEnabled.collectAsState()

    // If user is not logged in, navigate to login screen
    if (user == null) {
        LaunchedEffect(user) {
            navController.navigate(Screens.LOGIN.name) {
                popUpTo(Screens.USER_CONFIG.name) { inclusive = true }
            }
        }
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.main)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Padding.medium, Alignment.Top)
    ) {
        // User Info
        Text(
            text = stringResource(R.string.user_profile),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        
        Spacer(modifier = Modifier.height(Padding.medium))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier.padding(Padding.large),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                Text(
                    text = stringResource(R.string.name_format, user?.displayName ?: stringResource(R.string.user_default)),
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = stringResource(R.string.email_format, user?.email ?: stringResource(R.string.no_email)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Padding.medium))
        
        // Notification Settings Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier.padding(Padding.large),
                verticalArrangement = Arrangement.spacedBy(Padding.medium)
            ) {
                Text(
                    text = stringResource(R.string.notification_settings),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.daily_currency_reminder),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { scheduleNotificationViewModel.updateNotificationsEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                
                if (notificationsEnabled) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(Padding.small))
                        Text(
                            text = stringResource(R.string.notifications_enabled_time),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = Padding.small))

                // Test Notification Button
                Button(
                    onClick = { scheduleNotificationViewModel.scheduleTestNotification() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Box.Height.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(Padding.small))
                    Text(
                        text = stringResource(R.string.test_notification),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Text(
                    text = stringResource(R.string.test_notification_desc),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = Padding.extraSmall)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Padding.medium))
        
        // Logout Section
        Button(
            onClick = { 
                userViewModel.signOut()
                navController.navigate(Screens.LOGIN.name) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Box.Height.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(Padding.small))
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
