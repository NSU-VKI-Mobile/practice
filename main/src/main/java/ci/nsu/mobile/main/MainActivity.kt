package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.token.TokenManager
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем TokenManager с контекстом
        TokenManager.init(applicationContext)

        setContent {
            PracticeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Если токен уже есть — сразу на главный экран
    val startDestination = if (TokenManager.token != null) "main" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            MainScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
