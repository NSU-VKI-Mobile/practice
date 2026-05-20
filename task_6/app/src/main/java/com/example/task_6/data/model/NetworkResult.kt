package com.example.task_6.data.model

// Сел для представления результата сетевого запроса
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
    data object Idle : NetworkResult<Nothing>()
}