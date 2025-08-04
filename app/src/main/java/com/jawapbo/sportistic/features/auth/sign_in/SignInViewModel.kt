package com.jawapbo.sportistic.features.auth.sign_in

import com.jawapbo.sportistic.core.data.AuthDataStoreManager
import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.features.auth.AuthViewModel
import io.ktor.client.HttpClient

@Suppress("CanBeParameter")
class SignInViewModel(
    private val auth: AccountService,
    private val httpClient: HttpClient,
    private val authDataStoreManager: AuthDataStoreManager
): AuthViewModel(auth, httpClient, authDataStoreManager) {

}