package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repositories.AuthRepository
import ci.nsu.mobile.main.data.repositories.DepositRepository
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.SplashScreen
import ci.nsu.mobile.main.ui.screens.auth.LoginScreen
import ci.nsu.mobile.main.ui.screens.auth.RegisterScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.utils.UserPreferences
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.MyCalculationsViewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    // Dependencies
    val userPreferences = remember { UserPreferences(context.applicationContext) }
    val database = remember { AppDatabase.getDatabase(context.applicationContext) }
    val depositRepository = remember { DepositRepository(database.depositDao()) }
    val authRepository = remember { AuthRepository(userPreferences) }

    // ViewModels
    val authViewModel = AuthViewModel(authRepository)
    val depositViewModel = DepositViewModel(depositRepository, userPreferences)
    val myCalculationsViewModel = MyCalculationsViewModel(depositRepository, userPreferences)
    val usersViewModel = UsersViewModel(authRepository)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(
                userPreferences = userPreferences,
                onAuthenticated = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Auth Screens
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
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

        // Main App (with BottomNavigation)
        composable("main") {
            MainScreen(
                onLogout = {
                    // Очищаем данные и возвращаемся на экран входа
                    val activity = context as? MainActivity
                    activity?.lifecycleScope?.launch {
                        userPreferences.clear()
                    }
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                depositViewModel = depositViewModel,
                myCalculationsViewModel = myCalculationsViewModel,
                usersViewModel = usersViewModel
            )
        }
    }
}