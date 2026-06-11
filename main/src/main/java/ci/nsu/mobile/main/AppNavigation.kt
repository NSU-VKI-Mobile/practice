package ci.nsu.mobile.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.auth.data.remote.TokenManager
import ci.nsu.mobile.auth.data.remote.UserManager
import ci.nsu.mobile.auth.ui.login.LoginScreen
import ci.nsu.mobile.auth.ui.login.LoginViewModel
import ci.nsu.mobile.auth.ui.register.RegisterScreen
import ci.nsu.mobile.auth.ui.register.RegisterViewModel
import ci.nsu.mobile.calculations.ui.CalculationsScreen
import ci.nsu.mobile.calculations.ui.CalculationsViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(serviceLocator: ServiceLocator) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val viewModel = remember { LoginViewModel(serviceLocator.authRepository) }
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            val viewModel = remember { RegisterViewModel(serviceLocator.authRepository) }
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("main") {
            var selectedTab by remember { mutableIntStateOf(0) }

            val calculationsViewModel = remember {
                CalculationsViewModel(
                    provider = serviceLocator.depositRepository,
                    userId = UserManager.userId
                )
            }

            val usersViewModel = remember {
                UsersViewModel(serviceLocator.authRepository)
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Расчёт вкладов") },
                        actions = {
                            TextButton(onClick = {
                                serviceLocator.authRepository.logout()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }) {
                                Text("Выйти", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = {},
                            label = { Text("Пользователи") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = {},
                            label = { Text("Расчёты") }
                        )
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (selectedTab) {
                        0 -> UsersScreen(viewModel = usersViewModel)
                        1 -> CalculationsScreen(viewModel = calculationsViewModel)
                    }
                }
            }
        }
    }
}
