package ci.nsu.mobile.main.Views

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
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