package com.example.currencies.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.R
import com.example.currencies.auth.BiometricAuthManager
import com.example.currencies.ui.theme.Box
import com.example.currencies.ui.theme.Padding
import com.example.currencies.viewmodels.AuthViewModel

@Composable
fun BiometricLoginPage(
    onAuthSuccess: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val authViewModel = hiltViewModel<AuthViewModel>()
    val biometricAuthManager = remember { BiometricAuthManager() }

    // Check if biometric is available
    val isBiometricAvailable = biometricAuthManager.canAuthenticate(context)

    // Auto-trigger biometric authentication when the screen becomes visible
    LaunchedEffect(key1 = true) {
        if (!isBiometricAvailable) {
            // If biometric not available, proceed anyway
            // You could add different logic here like password fallback
            Toast.makeText(
                context,
                "Biometric authentication not available on this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = null,
            modifier = Modifier.size(Box.Width.large),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Padding.extraLarge))

        Text(
            text = stringResource(R.string.biometric_login),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Padding.small))

        Text(
            text = stringResource(R.string.biometric_prompt_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Padding.extraLarge))

        Button(
            onClick = {
                if (isBiometricAvailable) {
                    showBiometricPrompt(biometricAuthManager, context, onAuthSuccess)
                } else {
                    Toast.makeText(
                        context,
                        "Biometric authentication not available",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Could implement fallback here
                }
            }
        ) {
            Text(stringResource(R.string.use_fingerprint))
        }
    }
}

fun showBiometricPrompt(
    biometricAuthManager: BiometricAuthManager,
    context: android.content.Context,
    onSuccess: () -> Unit
) {
    biometricAuthManager.authenticate (
        context = context,
        onError = {
            Toast.makeText(context, "Authentication error", Toast.LENGTH_SHORT).show()
        },
        onSuccess = {
            Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show()
            onSuccess()
        },
        onFail = {
            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    )
}
