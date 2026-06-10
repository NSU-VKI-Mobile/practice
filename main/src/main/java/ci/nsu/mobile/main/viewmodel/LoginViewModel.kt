package ci.nsu.mobile.main.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.utils.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {

            loading = true
            error = null

            try {
                val result = repository.login(login, password)

                result.onSuccess {
                    Log.d("LOGIN", "Успешный вход")

                    TokenManager.token = it.token

                    Log.d("LOGIN", "Токен: ${it.token}")

                    onSuccess()
                }

                result.onFailure {
                    Log.e("LOGIN", "Ошибка входа", it)

                    error = it.message ?: "Ошибка входа"
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Исключение", e)

                error = e.message ?: "Неизвестная ошибка"
            }

            loading = false
        }
    }
}