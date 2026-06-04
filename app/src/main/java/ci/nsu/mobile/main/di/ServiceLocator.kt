package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.di.AuthManagerImpl
import ci.nsu.mobile.auth.di.AuthNavigatorImpl
import ci.nsu.mobile.auth.di.UserRepositoryImpl
import ci.nsu.mobile.auth.utils.UserPreferences
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.calculations.data.AppDatabase
import ci.nsu.mobile.calculations.di.CalculationsNavigatorImpl
import ci.nsu.mobile.calculations.di.CalculationsProviderImpl
import ci.nsu.mobile.calculations.repository.DepositRepository
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.CalculationsProvider
import ci.nsu.mobile.domain.interfaces.UserRepository
import ci.nsu.mobile.domain.navigation.AuthNavigator
import ci.nsu.mobile.domain.navigation.CalculationsNavigator

class ServiceLocator(private val context: Context) {


    val userPreferences: UserPreferences by lazy {
        UserPreferences(context.applicationContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(userPreferences)
    }


    val authManager: AuthManager by lazy {
        AuthManagerImpl(userPreferences, authRepository)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(authRepository)
    }

    val authViewModel: AuthViewModel by lazy {
        AuthViewModel(authRepository)
    }


    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    private val depositDao by lazy {
        database.depositDao()
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(depositDao)
    }

    val calculationsProvider: CalculationsProvider by lazy {
        CalculationsProviderImpl(depositRepository)
    }

}