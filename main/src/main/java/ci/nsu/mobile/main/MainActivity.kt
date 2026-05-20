package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.screen.LoginScreen
import ci.nsu.mobile.main.screen.MainScreen
import ci.nsu.mobile.main.screen.RegisterScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.utils.TokenManager
import ci.nsu.mobile.main.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        enableEdgeToEdge()
        setContent {

            PracticeTheme {
                val navController = rememberNavController()

                val repository = remember {
                    AuthRepository()
                }

                val viewModel = remember {
                    AuthViewModel(repository)
                }

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = {
                                    navController.navigate("main") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("main") {
                            MainScreen(
                                viewModel = viewModel,
                                onLogout = {
                                    viewModel.logout()

                                    navController.navigate("login") {
                                        popUpTo("main") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = viewModel,

                                onRegisterSuccess = {
                                    navController.popBackStack()
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}