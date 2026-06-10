package ci.nsu.mobile.main.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.dto.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val repository = AuthRepository()

    var users by mutableStateOf<List<UserDto>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        Log.d("USERS", "UsersViewModel created")
        loadUsers()
    }

    fun loadUsers() {

        Log.d("USERS", "loadUsers called")

        viewModelScope.launch {

            loading = true
            error = null

            try {

                Log.d("USERS", "requesting users")

                val result = repository.getUsers()

                result.onSuccess {
                    Log.d("USERS", "success, count=${it.size}")

                    users = it
                }

                result.onFailure {
                    Log.e("USERS", "request failed", it)

                    error = it.message ?: "Ошибка загрузки пользователей"
                }

            } catch (e: Exception) {

                Log.e("USERS", "exception", e)

                error = e.message ?: "Ошибка загрузки пользователей"
            }

            loading = false
        }
    }
}