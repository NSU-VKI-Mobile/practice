package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import ci.nsu.moble.main.api.ApiService
import ci.nsu.moble.main.api.AuthInterceptor
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.repositories.AuthRepository
import ci.nsu.moble.main.ui.AppNavigation
import ci.nsu.moble.main.ui.theme.MobilepracticeTheme
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.LoginScreenViewModel
import ci.nsu.moble.main.viewmodel.MainScreenViewModel
import ci.nsu.moble.main.viewmodel.RegisterViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dependencies
        val tokenManager = TokenManager(applicationContext)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
        val json = Json { ignoreUnknownKeys = true }
        val apiService = Retrofit.Builder()
            .baseUrl("http://10.0.0.2:8080")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
        val repository = AuthRepository(apiService)

        // create ViewModel
        @Suppress("UNCHECKED_CAST")
        val viewModel: AuthViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return AuthViewModel(repository, tokenManager) as T
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        val loginVm: LoginScreenViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val savedStateHandle = extras.createSavedStateHandle()
                    return LoginScreenViewModel(repository, tokenManager, savedStateHandle) as T
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        val mainScrVm: MainScreenViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainScreenViewModel(repository, tokenManager) as T
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        val registerVm: RegisterViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val savedStateHandle = extras.createSavedStateHandle()
                    return RegisterViewModel(repository, tokenManager, savedStateHandle) as T
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            MobilepracticeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(viewModel, loginVm, mainScrVm, registerVm,tokenManager)
                }
            }
        }
    }
}
