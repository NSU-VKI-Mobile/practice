package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.calculations.data.local.DepositDatabase
import ci.nsu.mobile.calculations.data.repository.DepositRepository

object ServiceLocator {
    val authRepository: AuthRepository by lazy { AuthRepository() }

    lateinit var depositRepository: DepositRepository
        private set

    fun init(context: Context) {
        val db = DepositDatabase.getDatabase(context)
        depositRepository = DepositRepository(db.depositDao())
    }
}