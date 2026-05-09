package ci.nsu.moble.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.ui.screens.LoginScreen
import ci.nsu.moble.main.ui.screens.MainScreen
import ci.nsu.moble.main.ui.screens.RegistrationScreen
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.LoginScreenViewModel
import ci.nsu.moble.main.viewmodel.MainScreenViewModel
import ci.nsu.moble.main.viewmodel.RegisterViewModel


@Composable
fun AppNavigation(authVm: AuthViewModel,
                  loginVm: LoginScreenViewModel,
                  mainScrVm: MainScreenViewModel,
                  registerVm: RegisterViewModel,
                  tokenManager: TokenManager) {
    val navController = rememberNavController()

    // Определяем стартовый экран: если токен есть — сразу в список
    val startDest = if (tokenManager.token != null) "main" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onNavToReg = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegistrationScreen(viewModel = registerVm, onBack = { navController.popBackStack() })
        }
        composable("main") {
            LaunchedEffect(Unit) { mainScrVm.loadUsers() }
            MainScreen(authVm = authVm, mainScrVm=mainScrVm, onLogout = {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            })
        }
    }
}