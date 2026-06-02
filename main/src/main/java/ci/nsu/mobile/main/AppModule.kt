package ci.nsu.mobile.main

import android.content.Context
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.token.TokenManager

class AppModule(private val context: Context) {

    // --- Компоненты Авторизации (Лабы 6-7) ---
    private val tokenManager by lazy {
        TokenManager(context.applicationContext)
    }

    private val apiService by lazy {
        RetrofitClient.getApiService(tokenManager)
    }

    val authRepository by lazy {
        AuthRepository(apiService, tokenManager)
    }

    // --- Компоненты локальной базы данных Room (Лаба 5) ---
    private val database by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    val depositRepository by lazy {
        DepositRepository(database.depositDao())
    }
}