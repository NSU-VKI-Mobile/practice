package ci.nsu.mobile.main.DI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.Data.Database.AppDatabase
import ci.nsu.mobile.main.Data.Database.DepositRepository
import ci.nsu.mobile.main.Repository.AuthRepository
import ci.nsu.mobile.main.ViewModel.*

class ServiceLocator(private val context: Context) {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val depositDao by lazy {
        database.depositDao()
    }

    val depositRepository by lazy {
        DepositRepository(depositDao)
    }

    val authRepository by lazy {
        AuthRepository()
    }
}

class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DepositViewModel::class.java) ->
                DepositViewModel(serviceLocator.depositRepository) as T
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(serviceLocator.authRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}