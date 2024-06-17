package com.capstone.babymeter.util

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

object FirebaseAuthHelper {
    private val auth: FirebaseAuth = Firebase.auth

    fun register(email: String, password: String, callback: (result: AuthResult?, error: Exception?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result, null)
                } else {
                    callback(null, task.exception)
                }
            }
    }

    fun login(email: String, password: String, callback: (result: AuthResult?, error: Exception?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result, null)
                } else {
                    callback(null, task.exception)
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}