package ci.nsu.mobile.main

import android.content.Context
import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.token.TokenManager

class AppModule(private val context: Context) {

    private val tokenManager by lazy {
        TokenManager(context.applicationContext)
    }

    private val apiService by lazy {
        RetrofitClient.getApiService(tokenManager)
    }

    val authRepository by lazy {
        AuthRepository(apiService, tokenManager)
    }
}