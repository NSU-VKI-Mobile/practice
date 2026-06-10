package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.home.HomeScreen
import ci.nsu.mobile.main.ui.login.LoginScreen
import ci.nsu.mobile.main.ui.register.RegisterScreen
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.viewmodel.RegisterViewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModel

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {

            val vm: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = vm,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {

            val vm: RegisterViewModel = viewModel()

            RegisterScreen(
                viewModel = vm,
                onRegisterClick = {
                    navController.popBackStack() // возврат на логин
                }
            )
        }

        composable("home") {

            val vm: UsersViewModel = viewModel()

            HomeScreen(
                viewModel = vm,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) // очистить весь бэкстек
                    }
                }
            )
        }
    }
}