package ci.nsu.mobile.main.sl

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.dbo.AppDatabase
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.vm.ViewModelFactory

class ServiceLocator(private val context: Context) {

    val appContext: Application = context.applicationContext as Application
    val database: AppDatabase by lazy { AppDatabase.getDatabase(appContext) }

    val authRepository: AuthRepository by lazy { AuthRepository() }

    val depositRepository: DepositRepository by lazy { DepositRepository(database.depositDao()) }

    val viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(this) }

    companion object {
        @Volatile
        private var INSTANCE: ServiceLocator? = null

        fun getInstance(context: Context): ServiceLocator {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceLocator(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}