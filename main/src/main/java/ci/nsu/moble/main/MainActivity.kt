package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import ci.nsu.moble.main.di.ServiceLocator
import ci.nsu.moble.main.ui.screens.*
import ci.nsu.moble.main.ui.theme.PracticeTheme
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.AuthViewModelFactory
import ci.nsu.moble.main.viewmodel.DepositViewModel
import ci.nsu.moble.main.viewmodel.DepositViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Сервис локатор для зависимостей
    val serviceLocator = remember { ServiceLocator(context) }

    // Авторизация
    val authRepository = serviceLocator.authRepository
    val authFactory = AuthViewModelFactory(authRepository)
    val authViewModel: AuthViewModel = viewModel(factory = authFactory)

    // ID текущего пользователя (пока временно, потом будем получать из токена)
    var currentUserId by remember { mutableStateOf(0L) }

    // Состояние для навигации по нижнему меню
    var selectedTab by remember { mutableStateOf(0) }

    // Определяем, авторизован ли пользователь
    val isLoggedIn by remember { mutableStateOf(false) } // замени на реальную проверку

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main_tabs" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // TODO: получить userId из ответа сервера
                    currentUserId = 1L // временно
                    navController.navigate("main_tabs")
                },
                onNavigateToRegister = { navController.navigate("register") },
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        // Главный экран с нижней навигацией
        composable("main_tabs") {
            MainTabsScreen(
                userId = currentUserId,
                onLogout = {
                    navController.popBackStack("login", inclusive = false)
                }
            )
        }
    }
}