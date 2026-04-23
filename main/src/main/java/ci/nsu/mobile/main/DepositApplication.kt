package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.remote.RetrofitClient
import ci.nsu.mobile.main.data.repository.AuthRepositoryImpl
import ci.nsu.mobile.main.domain.repository.AuthRepository

class DepositApplication : Application() {

    lateinit var tokenManager: TokenManager
        private set

    lateinit var repository: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()

        tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(tokenManager)
        repository = AuthRepositoryImpl(apiService, tokenManager)
    }
}