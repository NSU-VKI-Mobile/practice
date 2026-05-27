package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.local.database.AppDatabase
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository

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