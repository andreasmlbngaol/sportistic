package com.jawapbo.sportistic.features.auth

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.shared.data.auth.AuthMethod
import com.jawapbo.sportistic.shared.data.auth.AuthTokens
import com.jawapbo.sportistic.shared.data.auth.LoginRequest
import com.jawapbo.sportistic.shared.data.core.AuthDataStoreManager
import com.jawapbo.sportistic.shared.data.core.SportisticRepository
import kotlinx.coroutines.launch

abstract class AuthViewModel(
    private val auth: AccountService,
    private val repository: SportisticRepository,
    private val authDataStoreManager: AuthDataStoreManager
): ViewModel() {
    suspend fun onSignInWithGoogle(
        credential: Credential,
        onSignInSuccess: () -> Unit
    ) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            auth.signInWithGoogle(googleIdTokenCredential.idToken) { token ->
                Log.d("LoginViewModel", "Success sign in with Google with Firebase: $token")
                viewModelScope.launch {
                    val success = loginToBackend(token, AuthMethod.GOOGLE)
                    if (success) {
                        Log.d("LoginViewModel", "Login to backend successful. Navigating to Home")
                        onSignInSuccess()
                    }
                }
            }
        }
    }

    suspend fun loginToBackend(
        idToken: String,
        method: AuthMethod
    ): Boolean {
        Log.d("LoginViewModel", "Logging in to backend")
        val response = repository.customerLogin(
            body = LoginRequest(idToken, method)
        ) ?: return false

        authDataStoreManager.saveAuth(
            tokens = AuthTokens(
                accessToken = response.tokens.accessToken,
                refreshToken = response.tokens.refreshToken
            ),
            user = response.user
        )

        return true
    }
}