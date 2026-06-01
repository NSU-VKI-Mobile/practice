package ci.nsu.mobile.app.di

import android.content.Context
import ci.nsu.mobile.auth.data.ApiService
import ci.nsu.mobile.auth.data.AuthInterceptor
import ci.nsu.mobile.auth.data.AuthManagerImpl
import ci.nsu.mobile.auth.data.TokenManager
import ci.nsu.mobile.calculations.data.AppDatabase
import ci.nsu.mobile.calculations.data.CalculationsProviderImpl
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        TokenManager.init(context)
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
            .baseUrl("http://10.0.2.2:5000/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val authManager: AuthManager by lazy {
        AuthManagerImpl(apiService)
    }

    val calculationsProvider: CalculationsProvider by lazy {
        CalculationsProviderImpl(AppDatabase.getDatabase(getContext()).depositDao())
    }
}