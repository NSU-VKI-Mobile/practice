package ci.nsu.mobile.main

import android.content.Context
import ci.nsu.mobile.auth.data.remote.TokenManager
import ci.nsu.mobile.auth.data.remote.UserManager
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.calculations.data.local.database.AppDatabase
import ci.nsu.mobile.calculations.data.repository.DepositRepository

class ServiceLocator(private val context: Context) {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(database.depositDao())
    }
}