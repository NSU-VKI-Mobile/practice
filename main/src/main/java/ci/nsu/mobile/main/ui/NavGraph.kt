package ci.nsu.mobile.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.screens.UsersScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Users : Screen("users")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost( //NavHost - контейнер для навигации
        navController = navController,
        startDestination = Screen.Login.route // Стартовый экран
    ) {
        composable(Screen.Login.route) { //composable - привязывает маршрут к экрану
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Users.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } //Очищаем стек-историю (нельзя вернуться назад)
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) //navigate() - переход на другой экран
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Users.route) {
            UsersScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Users.route) { inclusive = true }
                    }
                }
            )
        }
    }
}