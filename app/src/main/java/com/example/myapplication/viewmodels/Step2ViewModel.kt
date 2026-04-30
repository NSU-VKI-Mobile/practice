package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel

class Step2ViewModel : ViewModel() {
    fun getRateOptions(months: Int): List<String> {
        return when {
            months < 6 -> listOf("15%")
            months < 12 -> listOf("10%")
            else -> listOf("5%")
        }
    }
}