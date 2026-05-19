package com.example.task4.viewmodel

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)