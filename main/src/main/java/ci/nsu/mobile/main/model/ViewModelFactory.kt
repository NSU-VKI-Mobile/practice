package ci.nsu.mobile.main.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(authRepository, tokenManager) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(authRepository, tokenManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

