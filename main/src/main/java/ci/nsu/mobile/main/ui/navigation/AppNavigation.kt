package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.login.LoginScreen
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.register.RegisterScreen
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.NavEvent
import ci.nsu.mobile.main.viewmodel.UserViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Создаем ViewModel, привязанную к NavGraph (живет пока жива навигация)
    val authViewModel: AuthViewModel = viewModel()

    // Слушаем события навигации от ViewModel
    LaunchedEffect(navController) {
        authViewModel.navEvent.collect { event ->
            when (event) {
                NavEvent.GoToMain -> navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.LOGIN) { inclusive = true } // Удаляем Login из истории, чтобы не вернуться кнопкой назад
                }
                NavEvent.GoToLogin -> navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.REGISTER) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        // Экран входа
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        // Экран регистрации
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Главный экран (список пользователей)
        composable(Routes.MAIN) {
            val userViewModel: UserViewModel = viewModel()

            // Слушаем событие выхода
            LaunchedEffect(navController) {
                userViewModel.logoutEvent.collect {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            }

            MainScreen(
                viewModel = userViewModel,
                onLogout = { userViewModel.logout() }
            )
        }
    }
}