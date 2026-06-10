package ci.nsu.mobile.practice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.ui.LoginScreen
import ci.nsu.mobile.auth.ui.RegisterScreen
import ci.nsu.mobile.auth.ui.UsersScreen
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.auth.viewmodel.GroupViewModel
import ci.nsu.mobile.auth.viewmodel.UsersViewModel
import ci.nsu.mobile.calculations.ui.DepositCalculatorScreen
import ci.nsu.mobile.calculations.ui.DepositDetailScreen
import ci.nsu.mobile.calculations.ui.DepositHistoryScreen
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.practice.di.ServiceLocator

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current                         // <-- получаем контекст
    val serviceLocator = ServiceLocator.getInstance(context)  // <-- передаём контекст
    val viewModelFactory = serviceLocator.viewModelFactory

    val isAuthenticated = TokenManager.token != null

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main_flow" else "login"
    ) {
        composable("login") {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main_flow") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
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

        composable("main_flow") {
            MainFlowScreen(
                onLogout = {
                    TokenManager.clear()
                    navController.navigate("login") {
                        popUpTo("main_flow") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainFlowScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val serviceLocator = ServiceLocator.getInstance(context)  // <-- снова передаём контекст
    val viewModelFactory = serviceLocator.viewModelFactory

    val userId = TokenManager.userId ?: 0L

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
                                popUpTo("users_list") { inclusive = false }
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
                val usersViewModel: UsersViewModel = viewModel(factory = viewModelFactory)
                UsersScreen(
                    usersViewModel = usersViewModel,
                    onLogout = onLogout
                )
            }

            composable("my_calculations") {
                val depositViewModel: DepositViewModel = viewModel(factory = viewModelFactory)
                DepositHistoryScreen(
                    depositViewModel = depositViewModel,
                    userId = userId,
                    onCalculationClick = { calculation ->
                        navController.navigate("calculation_detail/${calculation.id}")
                    }
                )
            }

            composable("new_calculation") {
                val depositViewModel: DepositViewModel = viewModel(factory = viewModelFactory)
                DepositCalculatorScreen(
                    depositViewModel = depositViewModel,
                    userId = userId,
                    onSaveSuccess = { navController.popBackStack() }
                )
            }

            composable(
                route = "calculation_detail/{calculationId}",
                arguments = listOf(navArgument("calculationId") { type = NavType.LongType })
            ) { backStackEntry ->
                val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L
                val depositViewModel: DepositViewModel = viewModel(factory = viewModelFactory)
                DepositDetailScreen(
                    depositViewModel = depositViewModel,
                    calculationId = calculationId,
                    userId = userId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Users : BottomNavItem("users_list", "Users", Icons.Default.Person)
    object MyCalculations : BottomNavItem("my_calculations", "History", Icons.Default.List)
    object NewCalculation : BottomNavItem("new_calculation", "New", Icons.Default.Add)
}