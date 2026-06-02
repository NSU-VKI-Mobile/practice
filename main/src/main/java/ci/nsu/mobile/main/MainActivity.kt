package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ui.screens.NavGraph
import ci.nsu.mobile.main.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var appModule: AppModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appModule = AppModule(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        val loginViewModel: LoginViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(appModule.authRepository) as T
                }
            }
        )

        val registerViewModel: RegisterViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegisterViewModel(appModule.authRepository) as T
                }
            }
        )

        val mainViewModel: MainViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(appModule.authRepository) as T
                }
            }
        )

        // --- ИНИЦИАЛИЗАЦИЯ ДЕПОЗИТНОЙ ВЬЮМОДЕЛИ ---
        val depositViewModel: DepositViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DepositViewModel(appModule.depositRepository) as T
                }
            }
        )

        NavGraph(
            loginViewModel = loginViewModel,
            registerViewModel = registerViewModel,
            mainViewModel = mainViewModel
            // Сюда мы передадим depositViewModel чуть позже, когда займёмся файлом NavGraph.kt
        )
    }
}