package com.capstone.babymeter.ui.history

import java.io.Serializable

data class HistoryItem(
    val name: String,
    val nik: String,
    val imageUrl: String
) : Serializable

