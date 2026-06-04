package ci.nsu.mobile.main

import android.content.Context
import ci.nsu.mobile.auth.AuthManagerImpl
import ci.nsu.mobile.auth.data.api.ApiClient
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.calculations.CalculationsProviderImpl
import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.db.AppDatabase
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.CalculationsProvider
import ci.nsu.mobile.main.navigation.AuthNavigatorImpl
import ci.nsu.mobile.main.navigation.CalculationsNavigatorImpl

class ServiceLocator private constructor(context: Context) {

    // :auth
    val authRepository: AuthRepository by lazy {
        AuthRepository(ApiClient.api)
    }
    val authManager: AuthManager by lazy { AuthManagerImpl() }
    val authNavigator: AuthNavigatorImpl by lazy { AuthNavigatorImpl() }

    // :calculations
    private val database: AppDatabase by lazy { AppDatabase.getInstance(context) }

    val depositDao: DepositDao by lazy { database.depositDao() }

    val calculationsProvider: CalculationsProvider by lazy {
        CalculationsProviderImpl(depositDao)
    }
    val calculationsNavigator: CalculationsNavigatorImpl by lazy {
        CalculationsNavigatorImpl()
    }

    companion object {
        @Volatile private var instance: ServiceLocator? = null

        fun init(context: Context) {
            if (instance == null) synchronized(this) {
                if (instance == null)
                    instance = ServiceLocator(context.applicationContext)
            }
        }

        fun get(): ServiceLocator =
            instance ?: error("ServiceLocator не инициализирован!")
    }
}