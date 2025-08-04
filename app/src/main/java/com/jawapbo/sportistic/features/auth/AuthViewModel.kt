package com.jawapbo.sportistic.features.auth

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.jawapbo.sportistic.core.data.AuthDataStoreManager
import com.jawapbo.sportistic.core.data.AuthTokens
import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.features.auth.data.AuthMethod
import com.jawapbo.sportistic.features.auth.data.LoginRequest
import com.jawapbo.sportistic.features.auth.data.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

abstract class AuthViewModel(
    private val auth: AccountService,
    private val httpClient: HttpClient,
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
        try {
            val response = httpClient.post("https://spring.sanalab.live/auth/login") {
                setBody(
                    LoginRequest(
                        idToken = idToken,
                        method = method
                    )
                )
            }

            if (response.status.isSuccess()) {
                val body = response.body<LoginResponse>()
                Log.d("LoginViewModel", "Login successful: ${body.user.id}")

                authDataStoreManager.saveAuth(
                    tokens = AuthTokens(
                        accessToken = body.tokens.accessToken,
                        refreshToken = body.tokens.refreshToken
                    ),
                    user = body.user
                )
                return true
            } else {
                Log.e("LoginViewModel", "Login failed: ${response.bodyAsText()}")
                return false
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error signing in: ${e.message}")
            return false
        }
    }
}