package com.capstone.babymeter.history

import java.io.Serializable

data class HistoryItem(
    val name: String,
    val nik: String,
    val beratBayi: String,
    val lingkarKepala: String,
    val lingkarBadan: String,
    val lingkarKaki: String,
    val tinggiBadan: String,
    val bmi: String,
    val category: String,
    val imageUrl: String
) : Serializable

