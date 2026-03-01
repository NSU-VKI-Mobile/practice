package com.example.practicenow.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicenow.data.model.LoginRequest
import com.example.practicenow.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    var login by mutableStateOf("")
    var password by mutableStateOf("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.login(LoginRequest(login, password))
            _isLoading.value = false

            result.onSuccess { onSuccess() }
                .onFailure { _error.value = "Ошибка входа: ${it.message}" }
        }
    }
}