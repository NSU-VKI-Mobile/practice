package ci.nsu.moble.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.ui.screens.LoginScreen
import ci.nsu.moble.main.ui.screens.MainTabsScreen
import ci.nsu.moble.main.ui.screens.RegistrationScreen
import org.koin.compose.koinInject


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Получаем TokenManager напрямую из Koin внутри Compose-контекста
    val tokenManager: TokenManager = koinInject()

    // Подписываемся на поток токена. При изменении токена Compose выполнит рекомпозицию!
    val tokenState by tokenManager.token.collectAsState()

    LaunchedEffect(tokenState) {
        if (tokenState == null) {
            // Если токен стерся (логаут или сброс сервера), принудительно уводим на логин
            navController.navigate("login") {
                popUpTo(0) { inclusive = true } // Очищаем абсолютно всю историю экранов
            }
        }
    }

    val startDest = remember { if (tokenState != null) "main_tabs" else "login" }

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(
                onNavToReg = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main_tabs") { popUpTo("login") { inclusive = true } }
                }
            )
        }

        composable("register") {
            RegistrationScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("main_tabs") {
            MainTabsScreen(
                onLogout = {
                    tokenManager.clear()
                    navController.navigate("login") { popUpTo("main_tabs") { inclusive = true } }
                }
            )
        }
    }
}