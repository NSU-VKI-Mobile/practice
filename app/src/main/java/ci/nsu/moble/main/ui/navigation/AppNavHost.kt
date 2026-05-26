package ci.nsu.moble.main.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.auth.ui.*
import ci.nsu.moble.auth.ui.login.LoginScreen
import ci.nsu.moble.auth.ui.login.LoginViewModel
import ci.nsu.moble.auth.ui.register.RegisterScreen
import ci.nsu.moble.auth.ui.register.RegisterViewModel
import ci.nsu.moble.auth.ui.users.UsersScreen
import ci.nsu.moble.auth.ui.users.UsersViewModel
import ci.nsu.moble.calculations.ui.DepositFlowScreen
import ci.nsu.moble.calculations.ui.DepositViewModel
import ci.nsu.moble.calculations.ui.HistoryScreen
import ci.nsu.moble.main.R
import ci.nsu.moble.main.di.AppModule

sealed class BottomNavItem(val route: String, val titleResId: Int) {
    object Users : BottomNavItem("users", R.string.screen_users)
    object History : BottomNavItem("history", R.string.screen_history)
    object NewDeposit : BottomNavItem("new_deposit", R.string.screen_new_deposit)
}

@Composable
fun AppNavHost(appModule: AppModule) {
    val navController = rememberNavController()
    val tokenManager = appModule.getTokenManager()
    val authRepository = appModule.getAuthRepository()
    val depositRepository = appModule.getDepositRepository()

    val isLoggedIn = tokenManager.isLoggedIn()
    val startDestination = if (isLoggedIn) {
        BottomNavItem.Users.route
    } else {
        "auth"
    }

    // State for managing navigation after auth
    var forceNavigateToUsers by remember { mutableStateOf(false) }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn && navController.currentDestination?.route == "auth") {
            forceNavigateToUsers = true
        }
    }

    LaunchedEffect(forceNavigateToUsers) {
        if (forceNavigateToUsers) {
            navController.navigate(BottomNavItem.Users.route) {
                popUpTo("auth") { inclusive = true }
            }
            forceNavigateToUsers = false
        }
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute in listOf(
                    BottomNavItem.Users.route,
                    BottomNavItem.History.route,
                    BottomNavItem.NewDeposit.route
                )
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == BottomNavItem.Users.route,
                        onClick = {
                            if (currentRoute != BottomNavItem.Users.route) {
                                navController.navigate(BottomNavItem.Users.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        icon = { Text("👥") },
                        label = { Text(stringResource(id = BottomNavItem.Users.titleResId)) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == BottomNavItem.History.route,
                        onClick = {
                            if (currentRoute != BottomNavItem.History.route) {
                                navController.navigate(BottomNavItem.History.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        icon = { Text("📋") },
                        label = { Text(stringResource(id = BottomNavItem.History.titleResId)) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == BottomNavItem.NewDeposit.route,
                        onClick = {
                            if (currentRoute != BottomNavItem.NewDeposit.route) {
                                navController.navigate(BottomNavItem.NewDeposit.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        icon = { Text("➕") },
                        label = { Text(stringResource(id = BottomNavItem.NewDeposit.titleResId)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("auth") {
                val loginViewModel: LoginViewModel = viewModel {
                    LoginViewModel(authRepository)
                }
                LoginScreen(
                    onLoginSuccess = {
                        forceNavigateToUsers = true
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    viewModel = loginViewModel
                )
            }

            composable("register") {
                val registerViewModel: RegisterViewModel = viewModel {
                    RegisterViewModel(authRepository)
                }
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.popBackStack()
                    },
                    viewModel = registerViewModel
                )
            }

            composable(BottomNavItem.Users.route) {
                val usersViewModel: UsersViewModel = viewModel {
                    UsersViewModel(authRepository)
                }
                UsersScreen(
                    viewModel = usersViewModel,
                    onLogout = {
                        tokenManager.clearAll()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomNavItem.History.route) {
                val userId = tokenManager.getUserId() ?: -1L
                val depositViewModel: DepositViewModel = viewModel {
                    DepositViewModel(userId, depositRepository)
                }
                val state by depositViewModel.uiState.collectAsState()
                HistoryScreen(
                    history = state.history,
                    onDelete = { depositViewModel.delete(it) }
                )
            }

            composable(BottomNavItem.NewDeposit.route) {
                val userId = tokenManager.getUserId() ?: -1L
                val depositViewModel: DepositViewModel = viewModel {
                    DepositViewModel(userId, depositRepository)
                }
                DepositFlowScreen(
                    viewModel = depositViewModel,
                    onFinish = {
                        navController.navigate(BottomNavItem.History.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}