package ci.nsu.mobile.main.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var login by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            isLoading = true

            repository.login(login, password)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    error = it.message
                }

            isLoading = false
        }
    }
}