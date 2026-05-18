package ci.nsu.mobile.main.Views

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedStateRegistryOwner
import ci.nsu.mobile.main.Auth.TokenManager
import ci.nsu.mobile.main.Network.ApiService
import ci.nsu.mobile.main.Network.RetrofitClient
import ci.nsu.mobile.main.Repository.AuthRepository
import ci.nsu.mobile.main.ViewModels.LoginViewModel
import ci.nsu.mobile.main.ViewModels.RegistrationViewModel
import ci.nsu.mobile.main.ViewModels.UserListViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tokenManager = TokenManager(applicationContext)
        apiService = RetrofitClient.getApiService(tokenManager)
        authRepository = AuthRepository(apiService, tokenManager)

        setContent {
            PracticeTheme {
                val navController = rememberNavController()
                // Общая ViewModel для навигации (можно и без неё)
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        val loginViewModel: LoginViewModel = viewModel(
                            factory = loginViewModelFactory(application, this@MainActivity, authRepository)
                        )
                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = { navController.navigate("userList") },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        val registerViewModel: RegistrationViewModel = viewModel(
                            factory = viewModelFactory { RegistrationViewModel(authRepository) }
                        )
                        RegistrationScreen(
                            viewModel = registerViewModel,
                            onRegisterSuccess = { navController.popBackStack() },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("userList") {
                        val userListViewModel: UserListViewModel = viewModel(
                            factory = viewModelFactory { UserListViewModel(authRepository) }
                        )
                        UserListScreen(
                            viewModel = userListViewModel,
                            onLogout = {
                                navController.popBackStack("login", inclusive = false)
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Вспомогательная функция для создания фабрики ViewModel
    private inline fun <reified VM : ViewModel> savedStateViewModelFactory(
        crossinline factory: (SavedStateHandle) -> VM
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory(SavedStateHandle()) as T
        }
    }

    private inline fun <reified VM : ViewModel> viewModelFactory(
        crossinline factory: () -> VM
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory() as T
        }
    }

    private fun loginViewModelFactory(
        application: Application,
        owner: SavedStateRegistryOwner,
        repository: AuthRepository
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @SuppressLint("RestrictedApi")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val savedStateHandle = SavedStateHandle.createHandle(
                owner.savedStateRegistry.consumeRestoredStateForKey("login"),
                null
            )
            return LoginViewModel(savedStateHandle, repository) as T
        }
    }
}