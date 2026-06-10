package ci.nsu.mobile.practice.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.auth.AuthManagerImpl
import ci.nsu.mobile.auth.AuthNavigatorImpl
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.network.ApiService as AuthApiService
import ci.nsu.mobile.auth.network.AuthInterceptor
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.auth.viewmodel.GroupViewModel
import ci.nsu.mobile.auth.viewmodel.UsersViewModel
import ci.nsu.mobile.calculations.CalculationsNavigatorImpl
import ci.nsu.mobile.calculations.CalculationsProviderImpl
import ci.nsu.mobile.calculations.data.local.AppDatabase
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.calculations.data.repository.DepositRepositoryImpl
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.domain.AuthManager
import ci.nsu.mobile.domain.AuthNavigator
import ci.nsu.mobile.domain.CalculationsNavigator
import ci.nsu.mobile.domain.CalculationsProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class ServiceLocator private constructor(
    private val applicationContext: Context,
    private var authNavigatorImpl: AuthNavigatorImpl? = null,
    private var calculationsNavigatorImpl: CalculationsNavigatorImpl? = null
) {

    companion object {
        @Volatile
        private var INSTANCE: ServiceLocator? = null

        fun getInstance(context: Context? = null): ServiceLocator {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceLocator(context!!.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // --- Room ---
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(applicationContext)
    }

    private val depositDao by lazy {
        database.depositDao()
    }

    // --- Network ---
    private val apiService: AuthApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.200.160:8080/api/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        retrofit.create(AuthApiService::class.java)
    }

    // --- Repositories ---
    private val authRepository: AuthRepository by lazy {
        AuthRepository(apiService)
    }

    private val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(depositDao)
    }

    // --- Managers / Providers ---
    lateinit var authManager: AuthManager
        private set
    lateinit var calculationsProvider: CalculationsProvider
        private set

    // --- Навигаторы (обновляются через setNavCallbacks) ---
    lateinit var authNavigator: AuthNavigator
        private set
    lateinit var calculationsNavigator: CalculationsNavigator
        private set

    /**
     * Вызовите этот метод из AppNavigation после создания navController,
     * чтобы передать конкретные действия для навигации.
     */
    fun setNavCallbacks(
        onNavigateToLogin: () -> Unit,
        onNavigateToRegister: () -> Unit,
        onNavigateToNewCalculation: (userId: Long) -> Unit,
        onNavigateToMyCalculations: (userId: Long) -> Unit
    ) {
        authNavigatorImpl = AuthNavigatorImpl(onNavigateToLogin, onNavigateToRegister)
        calculationsNavigatorImpl = CalculationsNavigatorImpl(onNavigateToNewCalculation, onNavigateToMyCalculations)

        authNavigator = authNavigatorImpl!!
        calculationsNavigator = calculationsNavigatorImpl!!

        // Пересоздаём менеджеры, которые могут зависеть от навигации (если нужно)
        authManager = AuthManagerImpl(authRepository)
        calculationsProvider = CalculationsProviderImpl(depositRepository)
    }

    // --- ViewModel Factory ---
    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(authRepository, depositRepository)
    }

    // Инициализация по умолчанию (пустые заглушки, чтобы не было NPE)
    init {
        // Временно назначаем пустые реализации
        authNavigator = AuthNavigatorImpl({}, {})
        calculationsNavigator = CalculationsNavigatorImpl({ _ -> }, { _ -> })
        authManager = AuthManagerImpl(authRepository)
        calculationsProvider = CalculationsProviderImpl(depositRepository)
    }
}

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val depositRepository: DepositRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepository) as T
            modelClass.isAssignableFrom(DepositViewModel::class.java) ->
                DepositViewModel(depositRepository) as T
            modelClass.isAssignableFrom(UsersViewModel::class.java) ->
                UsersViewModel(authRepository) as T
            modelClass.isAssignableFrom(GroupViewModel::class.java) ->
                GroupViewModel(authRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}