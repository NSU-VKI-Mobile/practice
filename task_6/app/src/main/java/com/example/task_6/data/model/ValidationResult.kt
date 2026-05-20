package com.example.task_6.data.model

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)