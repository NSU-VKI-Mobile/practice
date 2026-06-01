package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.remote.ApiService
import ci.nsu.mobile.main.data.remote.AuthInterceptor
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.AuthRepositoryImpl
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.repository.DepositRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        TokenManager.init(context) // Инициализируем менеджер токенов
    }

    private fun getContext(): Context {
        return appContext ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.20john_doe0.160:8080/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService)
    }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(getContext())
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(database.depositDao())
    }
}