package com.example.practicenow.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicenow.data.model.RegisterRequest
import com.example.practicenow.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    val isLoading = MutableStateFlow(false)

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            repository.register(request).onSuccess { onSuccess() }
            isLoading.value = false
        }
    }
}