package com.capstone.babymeter.model.response

data class PredictionData(
    val id: String,
    val babyName: String,
    val age: Int,
    val weight: Double,
    val lingkar_kepala: Double,
    val lingkar_dada: Double,
    val lingkar_lengan: Double,
    val lingkar_perut: Double,
    val lingkar_paha: Double,
    val panjang_badan: Double,
    val prediction: String,
    val confidence: Double,
    val suggestion: String,
    val createdAt: String,
    val updatedAt: String
)
