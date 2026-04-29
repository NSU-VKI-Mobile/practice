package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ci.nsu.mobile.main.network.RetrofitInstance
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.ui.theme.DraftTheme
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.screens.UserListScreen
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.GroupViewModel
import ci.nsu.mobile.main.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация TokenManager (теперь это синглтон)
        TokenManager.init(this)

        // Создаем ApiService и Repository
        val apiService = RetrofitInstance.createApiService()
        authRepository = AuthRepository(apiService)

        setContent {
            DraftTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        authRepository = authRepository
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    authRepository: AuthRepository
) {
    var isAuthenticated by remember { mutableStateOf(authRepository.isAuthenticated()) }

    val authViewModel: AuthViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AuthViewModel(authRepository)
            }
        }
    )

    val userViewModel: UserViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                UserViewModel(authRepository)
            }
        }
    )

    val groupViewModel: GroupViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                GroupViewModel(authRepository)
            }
        }
    )

    when {
        isAuthenticated -> {
            UserListScreen(
                userViewModel = userViewModel,
                onLogout = {
                    authRepository.logout()
                    isAuthenticated = false
                }
            )
        }
        else -> {
            var showRegister by remember { mutableStateOf(false) }

            if (showRegister) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    groupViewModel = groupViewModel,
                    onRegisterSuccess = {
                        showRegister = false
                    },
                    onNavigateBack = {
                        showRegister = false
                    }
                )
            } else {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        isAuthenticated = true
                    },
                    onNavigateToRegister = {
                        showRegister = true
                    }
                )
            }
        }
    }
}