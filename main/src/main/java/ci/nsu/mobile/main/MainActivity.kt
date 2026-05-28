package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.network.RetrofitClient

class MainActivity : ComponentActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getApiService(tokenManager)
        authRepository = AuthRepository(apiService, tokenManager)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthApp()
                }
            }
        }
    }

    @Composable
    fun AuthApp() {
        var isLoginScreen by rememberSaveable { mutableStateOf(true) }
        var isMainScreen by rememberSaveable { mutableStateOf(false) }

        if (isMainScreen) {
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(authRepository)
            )
            MainScreen(
                viewModel = viewModel,
                onLogout = {
                    tokenManager.clearToken()
                    isMainScreen = false
                    isLoginScreen = true
                }
            )
        } else if (isLoginScreen) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(authRepository)
            )
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { isLoginScreen = false },
                onLoginSuccess = {
                    isLoginScreen = false
                    isMainScreen = true
                }
            )
        } else {
            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(authRepository)
            )
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = { isLoginScreen = true },
                onRegisterSuccess = {
                    isLoginScreen = true
                }
            )
        }
    }
}