package com.example.currencies.pages

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.BuildConfig
import com.example.currencies.R
import com.example.currencies.ui.theme.Border
import com.example.currencies.ui.theme.Padding
import com.example.currencies.ui.theme.Radius
import com.example.currencies.viewmodels.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleLoginPage(
    onGoogleLoginSuccess: () -> Unit
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    // If user is already logged in, proceed to biometric login
    if (isLoggedIn) {
        onGoogleLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(Padding.large))

        Text(
            text = stringResource(id = R.string.google_sign_in),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(Padding.extraLarge))

        GoogleButtonUI(
            onClick = {
                coroutineScope.launch {
                    try {
                        // Instantiate a Google sign-in request
                        val googleIdOption = GetGoogleIdOption.Builder()
                            // Your server's client ID
                            .setServerClientId(BuildConfig.CLIENT_WEB_ID)
                            // Only show accounts previously used to sign in
                            .setFilterByAuthorizedAccounts(false)
                            .build()

                        // Create the Credential Manager request
                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        // Launch Credential Manager UI
                        val result = credentialManager.getCredential(
                            context = context,
                            request = request
                        )

                        // Extract credential from the result
                        val credential = result.credential

                        // Check if credential is of type Google ID
                        if (credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                            // Create Google ID Token
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                            // Sign in to Firebase using the token
                            val firebaseCredential = GoogleAuthProvider
                                .getCredential(googleIdTokenCredential.idToken, null)

                            FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        onGoogleLoginSuccess()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Google sign-in failed: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } catch (e: GetCredentialException) {
                        Log.e("GoogleLogin", "Couldn't retrieve user's credentials", e)
                        Toast.makeText(
                            context,
                            "Failed to retrieve Google credentials: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
}

@Composable
private fun GoogleButtonUI(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(Radius.medium),
        border = BorderStroke(Border.medium, Color.LightGray),
        contentPadding = PaddingValues(horizontal = Padding.large, vertical = Padding.large)
    ) {
        // You'll need to add a Google logo icon to your resources
        // For now, we'll use a placeholder icon if available
        Spacer(modifier = Modifier.width(Padding.small))

        Text(stringResource(id = R.string.continue_with_google))
    }
}
