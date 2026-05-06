package ci.nsu.mobile.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.auth.LoginScreen
import ci.nsu.mobile.main.ui.auth.RegisterScreen
import ci.nsu.mobile.main.ui.deposit.DepositCalculatorScreen
import ci.nsu.mobile.main.ui.deposit.DepositDetailScreen
import ci.nsu.mobile.main.ui.deposit.DepositHistoryScreen
import ci.nsu.mobile.main.ui.users.UsersScreen
import ci.nsu.mobile.main.viewmodel.*


@Composable
fun AppNavigation(viewModelFactory: androidx.lifecycle.ViewModelProvider.Factory) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    val isAuthenticated = TokenManager.token != null

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "users" else "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("users") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            val groupViewModel: GroupViewModel = viewModel(factory = viewModelFactory)
            RegisterScreen(
                authViewModel = authViewModel,
                groupViewModel = groupViewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("users") {
            val usersViewModel: UsersViewModel = viewModel(factory = viewModelFactory)
            MainScreenWithBottomBar(
                navController = navController,
                usersViewModel = usersViewModel,
                depositViewModel = viewModel(factory = viewModelFactory),
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun MainScreenWithBottomBar(
    navController: androidx.navigation.NavHostController,
    usersViewModel: UsersViewModel,
    depositViewModel: DepositViewModel,
    authViewModel: AuthViewModel
) {
    val items = listOf(
        BottomNavItem.Users,
        BottomNavItem.MyCalculations,
        BottomNavItem.NewCalculation
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("users") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "users_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("users_list") {
                UsersScreen(
                    usersViewModel = usersViewModel,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("users") { inclusive = true }
                        }
                    }
                )
            }

            composable("my_calculations") {
                DepositHistoryScreen(
                    depositViewModel = depositViewModel,
                    onCalculationClick = { calculation ->
                        navController.navigate("calculation_detail/${calculation.id}")
                    }
                )
            }

            composable("new_calculation") {
                DepositCalculatorScreen(
                    depositViewModel = depositViewModel
                )
            }

            composable(
                route = "calculation_detail/{calculationId}",
                arguments = listOf(navArgument("calculationId") { type = NavType.LongType })
            ) { backStackEntry ->
                val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L
                DepositDetailScreen(
                    depositViewModel = depositViewModel,
                    calculationId = calculationId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Users : BottomNavItem("users_list", "Пользователи", Icons.Default.Person)
    object MyCalculations : BottomNavItem("my_calculations", "Мои расчёты", Icons.Default.List)
    object NewCalculation : BottomNavItem("new_calculation", "Новый расчёт", Icons.Default.Add)
}