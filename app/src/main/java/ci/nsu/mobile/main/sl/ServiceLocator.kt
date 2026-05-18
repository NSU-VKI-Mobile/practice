package ci.nsu.mobile.main.sl

import android.app.Application
import android.content.Context
import ci.nsu.mobile.main.vm.ViewModelFactory
import com.example.auth.data.repository.AuthRepository
import com.example.calculations.data.db.AppDatabase
import com.example.calculations.data.repository.DepositRepository

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