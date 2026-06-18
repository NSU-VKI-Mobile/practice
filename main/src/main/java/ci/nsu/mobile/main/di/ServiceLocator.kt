package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.remote.RetrofitClient
import ci.nsu.mobile.main.data.repository.AuthRepositoryImpl
import ci.nsu.mobile.main.data.repository.DepositRepositoryImpl
import ci.nsu.mobile.main.domain.repository.AuthRepository
import ci.nsu.mobile.main.domain.repository.DepositRepository
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase

class ServiceLocator(context: Context) {

    val tokenManager: TokenManager = TokenManager(context)

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    private val apiService by lazy {
        RetrofitClient.create(tokenManager)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService, tokenManager)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(database.depositDao())
    }

    val calculateDepositUseCase: CalculateDepositUseCase by lazy {
        CalculateDepositUseCase()
    }
}