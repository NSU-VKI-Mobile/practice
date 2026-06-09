package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    private val _currentUser =
        MutableStateFlow<UserDto?>(null)

    val currentUser: StateFlow<UserDto?>
        get() = _currentUser

    private val _users =
        MutableStateFlow<List<UserDto>>(emptyList())

    val users: StateFlow<List<UserDto>>
        get() = _users

    private val _groups =
        MutableStateFlow<List<GroupDto>>(emptyList())

    val groups: StateFlow<List<GroupDto>>
        get() = _groups

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading.value = true
            error.value = null

            repository.login(login, password)
                .onSuccess { user ->

                    _currentUser.value = user

                    onSuccess()
                }
                .onFailure {
                    error.value = it.message
                }

            loading.value = false
        }
    }

    fun register(
        request: RegisterRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading.value = true
            error.value = null

            repository.register(request)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    error.value = it.message
                }

            loading.value = false
        }
    }

    fun logout() {

        TokenManager.clear()

        _currentUser.value = null
        _users.value = emptyList()
    }

    fun loadGroups() {

        viewModelScope.launch {

            repository.getGroups()
                .onSuccess {
                    _groups.value = it
                }
        }
    }

    fun loadUsers() {

        viewModelScope.launch {

            repository.getUsers()
                .onSuccess {
                    _users.value = it
                }
        }
    }


}
