package ci.nsu.mobile.main.sl

import android.annotation.SuppressLint
import android.content.Context
import ci.nsu.mobile.main.data.dbo.AppDatabase
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository

class ServiceLocator(private val context: Context) {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }

    val authRepository: AuthRepository by lazy { AuthRepository() }

    val depositRepository: DepositRepository by lazy { DepositRepository(database.depositDao()) }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ServiceLocator? = null

        fun getInstance(context: Context): ServiceLocator {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceLocator(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}