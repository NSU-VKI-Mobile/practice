package ci.nsu.mobile.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.domain.AuthRepository
import ci.nsu.mobile.main.data.storage.TokenManager
import ci.nsu.mobile.main.ui.login.LoginScreen
import ci.nsu.mobile.main.ui.login.LoginViewModel
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.main.MainViewModel
import ci.nsu.mobile.main.ui.register.RegisterScreen
import ci.nsu.mobile.main.ui.register.RegisterViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authRepository: AuthRepository,
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            val viewModel = viewModel { LoginViewModel(authRepository) }
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = Screen.Register.route) {
            val registerViewModel = viewModel { RegisterViewModel(authRepository) }
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                viewModel = registerViewModel
            )
        }

        composable(route = Screen.Main.route) {
            val mainViewModel = viewModel { MainViewModel(authRepository) }
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                viewModel = mainViewModel
            )
        }
    }
}