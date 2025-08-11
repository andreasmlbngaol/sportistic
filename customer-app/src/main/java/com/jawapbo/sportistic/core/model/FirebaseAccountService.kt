package com.jawapbo.sportistic.core.model

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class FirebaseAccountService(): AccountService {
    private val _auth = Firebase.auth

    override val current: FirebaseUser?
        get() = _auth.currentUser

    override suspend fun signInWithGoogle(
        idToken: String,
        onGetFirebaseIdToken: (String) -> Unit
    ) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        _auth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener {
                val firebaseUser = it.user
                firebaseUser?.getIdToken(true)
                    ?.addOnSuccessListener { result ->
                        val idToken = result.token
                        idToken?.let { token ->
                            onGetFirebaseIdToken(token)
                        }
                    }
            }
    }

    override suspend fun signOut() {
        _auth.signOut()
    }
}