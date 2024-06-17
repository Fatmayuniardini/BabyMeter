package com.capstone.babymeter.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.babymeter.model.request.RegisterRequest
import com.capstone.babymeter.model.response.AuthResponse
import com.capstone.babymeter.model.response.LogoutResponse
import com.capstone.babymeter.util.FirebaseAuthHelper
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest

class AuthViewModel : ViewModel() {
    private val _authState = MutableLiveData<AuthState>().apply { value = AuthState.Idle }
    val authState: LiveData<AuthState> get() = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        FirebaseAuthHelper.login(email, password) { result, error ->
            if (result != null) {
                _authState.value = AuthState.Success
                val idToken = result.user?.getIdToken(false)?.result?.token
                // Do something with idToken if needed
            } else {
                _authState.value = AuthState.Error(error?.message ?: "Unknown error")
            }
        }
    }

    fun register(request: RegisterRequest) {
        _authState.value = AuthState.Loading
        FirebaseAuthHelper.register(request.email, request.password) { result, error ->
            if (result != null) {
                val user = result.user
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(request.name)
                    .build()
                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            _authState.value = AuthState.Success
                        } else {
                            _authState.value = AuthState.Error(profileTask.exception?.message ?: "Profile update failed")
                        }
                    }
            } else {
                _authState.value = AuthState.Error(error?.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        FirebaseAuthHelper.logout()
        _authState.value = AuthState.Idle
    }
}
