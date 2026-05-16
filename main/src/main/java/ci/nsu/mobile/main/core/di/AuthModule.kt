package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.auth.data.api.ApiService
import ci.nsu.mobile.main.auth.data.api.AuthInterceptor
import ci.nsu.mobile.main.auth.data.api.KotlinxSerializationConverterFactory
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.auth.data.repository.AuthRepository
import ci.nsu.mobile.main.auth.data.repository.GroupRepository
import ci.nsu.mobile.main.auth.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    // 1. TokenManager
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    // 2. OkHttpClient (с перехватчиками)
    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor(tokenManager)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 3. Json конвертер
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    // 4. Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // localhost "http://10.0.2.2:8080/" college "http://192.168.200.160:8080/"
            .client(okHttpClient)
            .addConverterFactory(KotlinxSerializationConverterFactory(json))
            .build()
    }

    // 5. ApiService
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // 6. AuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(
        tokenManager: TokenManager,
        apiService: ApiService
    ): AuthRepository {
        return AuthRepository(tokenManager, apiService)
    }

    // 7. UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }

    //8. GroupRepository
    @Provides
    @Singleton
    fun provideGroupRepository(apiService: ApiService): GroupRepository {
        return GroupRepository(apiService)
    }
}