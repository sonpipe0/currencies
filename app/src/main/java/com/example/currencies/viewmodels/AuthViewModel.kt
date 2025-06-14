package com.example.currencies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // Authentication state
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Biometric authentication state
    // We start assuming NOT authenticated biometrically, even if logged in
    private val _isBiometricAuthenticated = MutableStateFlow(false)
    val isBiometricAuthenticated = _isBiometricAuthenticated.asStateFlow()

    init {
        // Listen for Firebase auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val userLoggedIn = firebaseAuth.currentUser != null
            _isLoggedIn.value = userLoggedIn

            // If user logs out, reset biometric authentication
            if (!userLoggedIn) {
                _isBiometricAuthenticated.value = false
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _isBiometricAuthenticated.value = false
    }

    fun setBiometricAuthenticated(authenticated: Boolean) {
        viewModelScope.launch {
            _isBiometricAuthenticated.value = authenticated
        }
    }
}
