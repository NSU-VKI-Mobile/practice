package ci.nsu.moble.main.di

import android.content.Context
import ci.nsu.moble.auth.data.network.NetworkModule
import ci.nsu.moble.auth.data.repository.AuthRepository
import ci.nsu.moble.auth.data.storage.TokenManager
import ci.nsu.moble.calculations.data.database.DepositDatabase
import ci.nsu.moble.calculations.data.repository.DepositRepository
import ci.nsu.moble.main.navigation.AuthNavigatorImpl
import ci.nsu.moble.main.navigation.CalculationsNavigatorImpl
import ci.nsu.moble.domain.interfaces.AuthNavigator
import ci.nsu.moble.domain.interfaces.CalculationsNavigator

class AppModule(private val context: Context) {

    private var _tokenManager: TokenManager? = null
    private var _authRepository: AuthRepository? = null
    private var _depositRepository: DepositRepository? = null
    private var _authNavigator: AuthNavigator? = null
    private var _calculationsNavigator: CalculationsNavigator? = null

    fun getTokenManager(): TokenManager {
        if (_tokenManager == null) {
            _tokenManager = TokenManager(context)
        }
        return _tokenManager!!
    }

    fun getAuthRepository(): AuthRepository {
        if (_authRepository == null) {
            val tokenManager = getTokenManager()
            val apiService = NetworkModule.provideApiService(tokenManager)
            _authRepository = AuthRepository(apiService, tokenManager)
        }
        return _authRepository!!
    }

    fun getDepositRepository(): DepositRepository {
        if (_depositRepository == null) {
            val database = DepositDatabase.getDb(context)
            val dao = database.dao()
            _depositRepository = DepositRepository(dao)
        }
        return _depositRepository!!
    }

    fun getAuthNavigator(): AuthNavigator {
        if (_authNavigator == null) {
            _authNavigator = AuthNavigatorImpl()
        }
        return _authNavigator!!
    }

    fun getCalculationsNavigator(): CalculationsNavigator {
        if (_calculationsNavigator == null) {
            _calculationsNavigator = CalculationsNavigatorImpl()
        }
        return _calculationsNavigator!!
    }
}