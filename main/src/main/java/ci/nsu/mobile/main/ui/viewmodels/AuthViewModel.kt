package ci.nsu.mobile.main.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Заполните все поля")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(login, password)
            _authState.value = if (result.isSuccess) AuthState.Success
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
        }
    }

    fun register(
        login: String, password: String, email: String, phoneNumber: String,
        firstName: String, lastName: String, middleName: String,
        birthDate: String, gender: String, groupId: Int
    ) {
        if (login.isBlank() || password.isBlank() || email.isBlank() ||
            firstName.isBlank() || lastName.isBlank() || birthDate.isBlank()
        ) {
            _authState.value = AuthState.Error("Заполните обязательные поля")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val request = RegisterRequest(
                login = login,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName.ifBlank { null },
                    birthDate = birthDate,
                    gender = gender,
                    groupId = groupId
                )
            )
            val result = repository.register(request)
            _authState.value = if (result.isSuccess) AuthState.Success
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка регистрации")
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            if (result.isSuccess) _groups.value = result.getOrDefault(emptyList())
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
