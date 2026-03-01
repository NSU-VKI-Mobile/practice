package com.example.practicenow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicenow.data.local.TokenManager
import com.example.practicenow.data.model.UserDto
import com.example.practicenow.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers().onSuccess {
                _users.value = it
            }.onFailure {
                // Ошибка обработки
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}