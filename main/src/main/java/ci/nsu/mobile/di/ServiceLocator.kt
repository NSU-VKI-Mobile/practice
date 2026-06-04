package ci.nsu.mobile.di

import android.content.Context
import ci.nsu.mobile.data.db.AppDatabase
import ci.nsu.mobile.data.repository.AuthRepository
import ci.nsu.mobile.data.repository.DepositRepository

class ServiceLocator(context: Context) {

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    val authRepository by lazy {
        AuthRepository()
    }

    val depositRepository by lazy {
        DepositRepository(database.depositDao())
    }
}