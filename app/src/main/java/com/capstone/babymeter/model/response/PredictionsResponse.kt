package com.capstone.babymeter.model.response

data class PredictionsResponse(
    val status: String,
    val data: List<PredictionData>?
)
