package com.example.app.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        loadUsers()
    }


    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val users = authManager.getUsers()
                _state.update {
                    it.copy(
                        users = users,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ошибка загрузки пользователей: ${e.message}"
                    )
                }
            }
        }
    }
}