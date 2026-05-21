package ci.nsu.mobile.main.units.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.units.data.model.UserDto
import ci.nsu.mobile.main.units.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel(){
    var users by mutableStateOf<List<UserDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var logoutTrigger by mutableStateOf(false)

    init {
        loadUsers()
    }
    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            repository.getUsers()
                .onSuccess { users = it }
                .onFailure {  }
            isLoading = false
        }
    }
    fun logout() {
        repository.logout()
        logoutTrigger = true
    }
}