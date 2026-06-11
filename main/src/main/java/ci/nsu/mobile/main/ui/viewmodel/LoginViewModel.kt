package ci.nsu.mobile.main.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun performLogin() {
        if (login.isBlank() || password.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.login(login, password)
                .onSuccess { loginSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}