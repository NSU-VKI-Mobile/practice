package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repositories.AuthRepository
import ci.nsu.mobile.main.data.repositories.DepositRepository
import ci.nsu.mobile.main.utils.UserPreferences

class ServiceLocator(private val context: Context) {

    // UserPreferences (хранилище токена и userId)
    private val userPreferences: UserPreferences by lazy {
        UserPreferences(context.applicationContext)
    }

    // База данных Room
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    // DAO
    private val depositDao by lazy {
        database.depositDao()
    }

    // Репозитории
    val authRepository: AuthRepository by lazy {
        AuthRepository(userPreferences)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(depositDao)
    }

    // Доступ к UserPreferences (для ViewModel, которые его используют)
    fun getUserPreferences(): UserPreferences = userPreferences
}