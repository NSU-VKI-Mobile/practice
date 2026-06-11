package ci.nsu.moble.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.data.api.RetrofitClient        // добавлено
import ci.nsu.moble.main.data.repository.AuthRepository
import ci.nsu.moble.main.data.storage.TokenManager
import ci.nsu.moble.main.ui.screens.LoginScreen
import ci.nsu.moble.main.ui.screens.MainScreen
import ci.nsu.moble.main.ui.screens.RegisterScreen
import ci.nsu.moble.main.ui.theme.MyAppTheme
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val tokenManager by lazy { TokenManager(applicationContext) }
    private val repository by lazy { AuthRepository(RetrofitClient.apiService, tokenManager) }
    private val authViewModel by lazy { AuthViewModel(repository) }
    private val mainViewModel by lazy { MainViewModel(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                AppNavigation(authViewModel, mainViewModel, repository)
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    repository: AuthRepository
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate("main") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                    navController.navigate("login")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                mainViewModel = mainViewModel,
                onLogout = {
                    repository.logout()
                    navController.popBackStack("login", inclusive = false)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}