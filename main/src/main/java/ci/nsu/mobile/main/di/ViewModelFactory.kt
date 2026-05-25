package ci.nsu.mobile.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.UsersViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(ServiceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                DepositViewModel(ServiceLocator.depositRepository, ServiceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(UsersViewModel::class.java) -> {
                UsersViewModel(ServiceLocator.authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}