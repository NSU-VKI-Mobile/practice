package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _users =
        MutableStateFlow<List<UserDto>>(emptyList())

    val users: StateFlow<List<UserDto>>
        get() = _users

    private val _groups =
        MutableStateFlow<List<GroupDto>>(emptyList())

    val groups: StateFlow<List<GroupDto>>
        get() = _groups

    var loading =
        MutableStateFlow(false)

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading.value = true

            repository.login(login, password)
                .onSuccess {
                    onSuccess()
                }

            loading.value = false
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

    fun loadGroups() {

        viewModelScope.launch {

            repository.getGroups()
                .onSuccess {
                    _groups.value = it
                }
        }
    }
}