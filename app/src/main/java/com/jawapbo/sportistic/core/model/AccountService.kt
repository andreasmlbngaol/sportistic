package com.jawapbo.sportistic.core.model

import com.google.firebase.auth.FirebaseUser

interface AccountService {
    val current: FirebaseUser?
    suspend fun signInWithGoogle(
        idToken: String,
        onGetFirebaseIdToken: (String) -> Unit
    )
    suspend fun signOut()
}