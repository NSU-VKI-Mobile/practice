package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.auth.utils.UserPreferences
import ci.nsu.mobile.auth.di.AuthManagerImpl
import ci.nsu.mobile.auth.di.AuthNavigatorImpl
import ci.nsu.mobile.calculations.data.AppDatabase
import ci.nsu.mobile.calculations.repository.DepositRepository
import ci.nsu.mobile.calculations.di.CalculationsProviderImpl
import ci.nsu.mobile.calculations.di.CalculationsNavigatorImpl
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.CalculationsProvider
import ci.nsu.mobile.domain.navigation.AuthNavigator
import ci.nsu.mobile.domain.navigation.CalculationsNavigator

class ServiceLocator(private val context: Context) {

    // Auth
    val authManager: AuthManager by lazy {
        AuthManagerImpl(UserPreferences(context.applicationContext))
    }

    val authNavigator: AuthNavigator by lazy {
        AuthNavigatorImpl()
    }

    // Calculations
    val calculationsProvider: CalculationsProvider by lazy {
        val database = AppDatabase.getDatabase(context.applicationContext)
        val repository = DepositRepository(database.depositDao())
        CalculationsProviderImpl(repository)
    }

    val calculationsNavigator: CalculationsNavigator by lazy {
        CalculationsNavigatorImpl()
    }
}