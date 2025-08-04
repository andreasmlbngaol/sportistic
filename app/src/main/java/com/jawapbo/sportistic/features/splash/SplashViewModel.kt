package com.jawapbo.sportistic.features.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawapbo.sportistic.core.data.AuthDataStoreManager
import com.jawapbo.sportistic.navigation.NavKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authDataStoreManager: AuthDataStoreManager
): ViewModel() {
    private val _startDestination = MutableStateFlow<NavKey?>(null)
    val startDestination = _startDestination.asStateFlow()

    fun checkAuthStatus() {
        viewModelScope.launch {
            delay(2000L)
            val user = authDataStoreManager.user

            _startDestination.value = if (user != null) {
                Log.d("SplashViewModel", "User is logged in (id : ${user.id}). Navigate to Home.")
                NavKey.Home
            } else {
                Log.d("SplashViewModel", "User is not logged in. Navigate to SignIn.")
                NavKey.SignIn
            }
        }
    }
}