package ci.nsu.mobile.main.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.dao.DepositDao
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.repository.DepositRepositoryImpl
import ci.nsu.mobile.main.network.ApiService
import ci.nsu.mobile.main.network.RetrofitInstance
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.GroupViewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModel

class ServiceLocator(private val context: Context) {

    // Room Database
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val depositDao: DepositDao by lazy {
        database.depositDao()
    }

    // API
    val apiService: ApiService by lazy {
        RetrofitInstance.createApiService()
    }

    // Repositories
    val authRepository: AuthRepository by lazy {
        AuthRepository(apiService)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(depositDao)
    }

    // ViewModel Factory
    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(this)
    }
}

class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(serviceLocator.authRepository) as T
            modelClass.isAssignableFrom(DepositViewModel::class.java) ->
                DepositViewModel(serviceLocator.depositRepository) as T
            modelClass.isAssignableFrom(UsersViewModel::class.java) ->
                UsersViewModel(serviceLocator.authRepository) as T
            modelClass.isAssignableFrom(GroupViewModel::class.java) ->
                GroupViewModel(serviceLocator.authRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}