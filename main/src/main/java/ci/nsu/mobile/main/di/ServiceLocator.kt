package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.local.database.AppDatabase
import ci.nsu.mobile.main.data.remote.TokenManager
import ci.nsu.mobile.main.data.remote.UserManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository

class ServiceLocator(private val context: Context) {

    val tokenManager: TokenManager by lazy {
        TokenManager.init(context)
        TokenManager
    }

    val userManager: UserManager by lazy {
        UserManager.init(context)
        UserManager
    }
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(tokenManager, userManager)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(database.depositDao())
    }

    val token: TokenManager get() = tokenManager
    val user: UserManager get() = userManager
}