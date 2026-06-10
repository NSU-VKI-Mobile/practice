package ci.nsu.data.di

import ci.nsu.data.local.AppDatabase
import ci.nsu.data.local.SessionManager
import ci.nsu.data.remote.AuthApiService
import ci.nsu.data.remote.AuthInterceptor
import ci.nsu.data.repository.AuthRepository
import ci.nsu.data.repository.AuthRepositoryImpl
import ci.nsu.data.repository.DepositRepository
import ci.nsu.data.repository.DepositRepositoryImpl
import ci.nsu.ui.auth.AuthViewModel
import ci.nsu.ui.deposit.DepositViewModel // Импортируем нашу новую ViewModel калькулятора
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    // --- ЛОКАЛЬНЫЙ СЛОЙ ---
    single { SessionManager(androidContext()) }
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().depositDao() }
    single<DepositRepository> { DepositRepositoryImpl(get()) }

    // --- СЕТЕВОЙ СЛОЙ ---
    single { AuthInterceptor(get()) }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://192.168.200.160:8080/api/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Создаем реализацию API интерфейса
    single { get<Retrofit>().create(AuthApiService::class.java) }

    // --- РЕПОЗИТОРИИ ---
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // --- VIEW MODELS ---
    viewModel { AuthViewModel(get()) }
    viewModel { DepositViewModel(get(), get()) } // Зарегистрировали фабрику калькулятора в Koin

    viewModel { AuthViewModel(get()) }
    viewModel { DepositViewModel(get(), get()) }
    viewModel { ci.nsu.ui.users.UsersViewModel(get()) }
}
