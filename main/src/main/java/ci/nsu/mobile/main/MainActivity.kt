package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.ui.screens.HomeScreen
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)

        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()

            // Определяем стартовый экран один раз при запуске
            val isAuthenticated by viewModel.isAuthenticated.collectAsState()
            val startDestination = if (isAuthenticated) "home" else "login"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") {
                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = { navController.navigate("register") },
                        onLoginSuccess = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                composable("register") {
                    RegisterScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        onLogoutClick = {
                            // Очищаем весь стек и идем на логин
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}