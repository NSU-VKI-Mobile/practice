package ci.nsu.mobile.main.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.network.NetworkModule
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.ui.AuthViewModel
import ci.nsu.mobile.main.ui.DepositViewModel

class ServiceLocator private constructor(
    context: Context
) {
    private val appContext = context.applicationContext

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(appContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(NetworkModule.authApi)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(database.depositDao())
    }

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        AppViewModelFactory(this)
    }

    companion object {
        @Volatile
        private var INSTANCE: ServiceLocator? = null

        fun get(context: Context): ServiceLocator {
            TokenManager.init(context.applicationContext)
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceLocator(context).also { INSTANCE = it }
            }
        }
    }
}

private class AppViewModelFactory(
    private val serviceLocator: ServiceLocator
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(serviceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                DepositViewModel(serviceLocator.depositRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
