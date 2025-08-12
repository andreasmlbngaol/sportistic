package com.jawapbo.sportistic.features.auth.sign_in

import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.features.auth.AuthViewModel
import com.jawapbo.sportistic.shared.data.core.AuthDataStoreManager
import com.jawapbo.sportistic.shared.data.core.SportisticRepository

@Suppress("CanBeParameter")
class SignInViewModel(
    private val auth: AccountService,
    private val sportisticRepository: SportisticRepository,
    private val authDataStoreManager: AuthDataStoreManager
): AuthViewModel(auth, sportisticRepository, authDataStoreManager) {

}