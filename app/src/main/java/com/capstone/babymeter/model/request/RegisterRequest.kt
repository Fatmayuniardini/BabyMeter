package com.capstone.babymeter.model.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
