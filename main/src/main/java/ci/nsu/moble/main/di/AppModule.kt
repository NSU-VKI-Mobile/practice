package ci.nsu.moble.main.di

import ci.nsu.moble.main.api.ApiService
import ci.nsu.moble.main.api.AuthInterceptor
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dbo.CalcDatabase
import ci.nsu.moble.main.data.repositories.AuthRepository
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.CalcScreensViewModel
import ci.nsu.moble.main.viewmodel.HistoryViewModel
import ci.nsu.moble.main.viewmodel.LoginScreenViewModel
import ci.nsu.moble.main.viewmodel.MainScreenViewModel
import ci.nsu.moble.main.viewmodel.RegisterViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    // 1. Хранилище токенов и сессий
    single { TokenManager(androidContext()) }

    // 2. Сетевой слой (Retrofit + OkHttpClient + Сериализация)
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(get())) // get() автоматически подставит TokenManager
            .build()
    }

    single { Json { ignoreUnknownKeys = true } }

    single<ApiService> {
        val contentType = "application/json".toMediaType()
        val json: Json = get()

        Retrofit.Builder()
            .baseUrl("http://10.0.0.2:8080")
            .client(get()) // get() подставит OkHttpClient
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }

    // 3. Репозитории
    single { AuthRepository(get()) } // get() подставит ApiService

    // 4. Локальная база данных Room и DAO
    single { CalcDatabase.getDatabase(androidApplication()) }
    single { get<CalcDatabase>().dao() } // Автоматически берет Dao из CalcDatabase

    // 5. Современный архитектурный слой (ViewModels) через Constructor DSL
    // Koin сам автоматически подставит репозитории, Dao и менеджеры в конструкторы!
    // Its life state depends on ViewModelStoreOwner's life state (so scoped)
    viewModelOf(::AuthViewModel)
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::CalcScreensViewModel)
    viewModelOf(::HistoryViewModel)

    // Для ViewModels с SavedStateHandle синтаксис остается точно таким же!
    // Koin 4 автоматически распознает SavedStateHandle в конструкторе и прокинет его.
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::RegisterViewModel)
}