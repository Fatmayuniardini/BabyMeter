package com.capstone.babymeter.model.response

data class AuthResponse(
    val status: Int,
    val message: String,
    val idToken: String?
)
