package ci.nsu.moble.main.di

import android.content.Context
import ci.nsu.moble.main.utils.TokenManager
import ci.nsu.moble.main.data.database.AppDatabase
import ci.nsu.moble.main.data.repository.AuthRepository
import ci.nsu.moble.main.data.repository.DepositRepository

class ServiceLocator(private val context: Context) {

    private val database by lazy {
        AppDatabase.getDatabase(context)
    }

    private val depositDao by lazy {
        database.depositDao()
    }

    val authRepository by lazy {
        AuthRepository(context)
    }

    val depositRepository by lazy {
        DepositRepository(depositDao)
    }

    val tokenManager by lazy {
        TokenManager(context)
    }
}