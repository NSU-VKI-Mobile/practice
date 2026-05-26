package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.remote.ApiService
import ci.nsu.mobile.main.data.remote.AuthInterceptor
import ci.nsu.mobile.main.data.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun getContext(): Context {
        return appContext ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    private val authRepositoryLazy: Lazy<AuthRepository> by lazy { lazy { authRepository } }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { authRepositoryLazy.value.getToken() })
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.200.160:8080/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(getContext())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService, getContext())
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(database.depositDao())
    }
}