package ci.nsu.mobile.main.model

import android.app.Application

class MyApplication : Application() {
    lateinit var authRepository: AuthRepository
    lateinit var tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()

        tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getInstance(tokenManager)
        authRepository = AuthRepositoryImpl(apiService)
    }
}
