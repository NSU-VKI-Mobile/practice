package ci.nsu.mobile.main.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var login by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val request = LoginRequest(login, password)

            repository.login(request)
                .onSuccess { onSuccess() }
                .onFailure {
                    error = when (it) {
                        is HttpException -> {
                            when (it.code()) {
                                401 -> "Неверный логин или пароль"
                                else -> "Ошибка сервера"
                            }
                        }

                        else -> {
                            it.message ?: "Неизвестная ошибка"
                        }
                    }
                }

            isLoading = false
        }
    }
}