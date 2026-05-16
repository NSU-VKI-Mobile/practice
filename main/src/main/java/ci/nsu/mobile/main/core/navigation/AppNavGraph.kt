package ci.nsu.mobile.main.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.auth.ui.login.LoginScreen
import ci.nsu.mobile.main.auth.ui.login.LoginViewModel
import ci.nsu.mobile.main.auth.ui.register.RegisterScreen
import ci.nsu.mobile.main.auth.ui.register.RegisterViewModel
import ci.nsu.mobile.main.auth.ui.users.UsersScreen
import ci.nsu.mobile.main.auth.ui.users.UsersViewModel

@Composable
fun AppNavGraph(
    tokenManager: TokenManager,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()

    var isLoggedIn by remember {
        mutableStateOf(tokenManager.token != null && tokenManager.userId != null)
    }

    LaunchedEffect(tokenManager.token) {
        isLoggedIn = tokenManager.token != null && tokenManager.userId != null
    }

    // Track current route to conditionally show bottom bar
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Routes that should show bottom navigation bar
    val mainRoutes = setOf(Routes.USERS, Routes.HISTORY, Routes.NEW_CALCULATION)
    val showBottomBar = isLoggedIn && mainRoutes.contains(currentRoute)
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Routes.USERS else Routes.LOGIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onLoginSuccess = {
                        isLoggedIn = true
                        navController.navigate(Routes.USERS) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    viewModel = loginViewModel
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        isLoggedIn = true
                        navController.navigate(Routes.USERS) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    viewModel = registerViewModel
                )
            }

            composable(Routes.USERS) {
                UsersScreen(
                    onLogout = {
                        tokenManager.clear()
                        isLoggedIn = false
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = usersViewModel
                )
            }

            composable(Routes.HISTORY) {
                EmptyScreen("Мои расчёты")
            }

            composable(Routes.NEW_CALCULATION) {
                EmptyScreen("Новый расчёт")
            }
        }
    }
}

@Composable
fun EmptyScreen(title: String = "TODO") {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}