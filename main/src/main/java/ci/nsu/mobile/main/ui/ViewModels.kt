package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.data.TokenManager
import ci.nsu.mobile.auth.ui.AuthViewModel
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.calculations.ui.DepositViewModel

class ViewModelFactory(
    private val authRepo: AuthRepository,
    private val depositRepo: DepositRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepo) as T
        }
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
            return DepositViewModel(depositRepo, TokenManager.userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}