package ci.nsu.mobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.RegisterScreen
import ci.nsu.mobile.auth.ui.screens.MainScreen
import ci.nsu.mobile.auth.ui.theme.PracticeTheme
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.auth.viewmodel.AuthViewModelFactory

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
    val repository = AuthRepository(context)
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") },
                onNavigateToRegister = { navController.navigate("register") },
                viewModel = viewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    navController.popBackStack("login", inclusive = false)
                },
                viewModel = viewModel
            )
        }
    }
}