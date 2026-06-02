package ci.nsu.mobile.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.screen.login.LoginScreen
import ci.nsu.mobile.main.ui.screen.login.LoginViewModel
import ci.nsu.mobile.main.ui.screen.main.MainScreen
import ci.nsu.mobile.main.ui.screen.main.MainViewModel
import ci.nsu.mobile.main.ui.screen.register.RegisterScreen
import ci.nsu.mobile.main.ui.screen.register.RegisterViewModel

@Composable
fun AppNavigation(serviceLocator: ServiceLocator) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val viewModel = remember {
                LoginViewModel(serviceLocator.authRepository)
            }
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            val viewModel = remember {
                RegisterViewModel(serviceLocator.authRepository)
            }
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            val viewModel = remember {
                MainViewModel(
                    authRepository = serviceLocator.authRepository,
                    depositRepository = serviceLocator.depositRepository
                )
            }
            MainScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}