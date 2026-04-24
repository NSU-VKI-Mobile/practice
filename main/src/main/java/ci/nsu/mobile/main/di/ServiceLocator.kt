package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.DepositRepository

object ServiceLocator {
    private var database: AppDatabase? = null

    val authRepository: AuthRepository by lazy { AuthRepository() }

    lateinit var depositRepository: DepositRepository
        private set

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        database = db
        depositRepository = DepositRepository(db.depositDao())
    }
}