package com.example.currencies.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.R
import com.example.currencies.ui.theme.Box
import com.example.currencies.ui.theme.Icon
import com.example.currencies.ui.theme.Padding
import com.example.currencies.ui.theme.Radius
import com.example.currencies.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseUser

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginPage(
    onLoginSuccess: () -> Unit
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val user by userViewModel.userData.collectAsState()
    
    // If user is already logged in, navigate to the main screen
    if (user != null) {
        onLoginSuccess()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.main),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Padding.large, Alignment.CenterVertically)
    ) {
        // App logo or title
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        
        Spacer(modifier = Modifier.height(Padding.large))
        
        // Google Sign-In Button
        Button(
            onClick = { userViewModel.launchCredentialManager() },
            modifier = Modifier
                .fillMaxWidth()
                .height(Box.Height.medium),
            shape = RoundedCornerShape(Radius.small),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Login,
                contentDescription = stringResource(R.string.login_icon_description),
                modifier = Modifier.size(Icon.normal)
            )
            Spacer(modifier = Modifier.width(Padding.small))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun UserInfoCard(user: FirebaseUser, onSignOut: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.large),
        elevation = CardDefaults.cardElevation(defaultElevation = Padding.small)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Padding.small)
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = stringResource(R.string.name_format, user.displayName ?: ""),
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = stringResource(R.string.email_format, user.email ?: ""),
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(Padding.small))

            Button(
                onClick = { onSignOut() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text(stringResource(R.string.sign_out))
            }
        }
    }
}
