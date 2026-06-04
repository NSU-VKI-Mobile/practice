package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.ui.AuthViewModel
import ci.nsu.mobile.calculations.ui.DepositViewModel
import ci.nsu.mobile.main.ServiceLocator

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