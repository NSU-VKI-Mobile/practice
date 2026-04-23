package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ServiceLocator
import ci.nsu.mobile.main.TokenManager

class AppViewModelFactory(private val sl: ServiceLocator) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) ->
            AuthViewModel(sl.authRepository) as T

        modelClass.isAssignableFrom(DepositViewModel::class.java) ->
            DepositViewModel(sl.depositDao, TokenManager.userId ?: 0L) as T

        else -> throw IllegalArgumentException("Неизвестный ViewModel: ${modelClass.name}")
    }
}