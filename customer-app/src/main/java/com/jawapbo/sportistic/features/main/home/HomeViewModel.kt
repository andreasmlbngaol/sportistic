package com.jawapbo.sportistic.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawapbo.sportistic.core.data.AuthDataStoreManager
import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.shared.data.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val accountService: AccountService,
    private val authDataStoreManager: AuthDataStoreManager
): ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch {
            authDataStoreManager.user?.let { usr ->
                _user.value = usr
            }
        }
    }

    fun onSignOut(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            authDataStoreManager.clearDataStore()
            accountService.signOut()
            onSuccess()
        }
    }
}