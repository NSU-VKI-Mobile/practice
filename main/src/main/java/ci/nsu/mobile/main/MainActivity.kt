package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.network.RetrofitClient
import ci.nsu.mobile.main.ui.login.LoginScreen
import ci.nsu.mobile.main.ui.login.LoginViewModel
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.main.MainViewModel
import ci.nsu.mobile.main.ui.register.RegisterScreen
import ci.nsu.mobile.main.ui.register.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitClient.instance
        val authRepository = AuthRepository(apiService)

        setContent {

            androidx.compose.material3.MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        val vm: LoginViewModel = viewModel(
                            factory = LoginViewModelFactory(authRepository)
                        )
                        LoginScreen(
                            viewModel = vm,
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
                        val vm: RegisterViewModel = viewModel(
                            factory = RegisterViewModelFactory(authRepository)
                        )
                        RegisterScreen(
                            viewModel = vm,
                            onRegistrationSuccess = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("main") {
                        val vm: MainViewModel = viewModel(
                            factory = MainViewModelFactory(authRepository)
                        )
                        MainScreen(
                            viewModel = vm,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}