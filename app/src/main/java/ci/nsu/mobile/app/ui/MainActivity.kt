package ci.nsu.mobile.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.app.di.ServiceLocator
import ci.nsu.mobile.auth.ui.AuthViewModel
import ci.nsu.mobile.auth.ui.LoginScreen
import ci.nsu.mobile.auth.ui.ProfileScreen
import ci.nsu.mobile.auth.ui.UsersScreen
import ci.nsu.mobile.calculations.ui.DepositViewModel
import ci.nsu.mobile.calculations.ui.HistoryScreen
import ci.nsu.mobile.calculations.ui.NewCalculationScreen
import ci.nsu.mobile.domain.model.UserDto

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Users : Screen("users", "Пользователи", Icons.Filled.People)
    object History : Screen("history", "Мои расчёты", Icons.Filled.History)
    object NewCalc : Screen("new_calc", "Новый расчёт", Icons.Filled.Add)
    object Profile : Screen("profile/{userId}", "Профиль", null) {
        fun createRoute(userId: Long) = "profile/$userId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authManager = ServiceLocator.authManager
    val calcProvider = ServiceLocator.calculationsProvider

    val isLoggedIn = authManager.isLoggedIn()

    if (!isLoggedIn) {
        LoginScreen(
            viewModel = viewModel { AuthViewModel(authManager) },
            onNavigateToRegister = { /* TODO: Add Register Navigation */ }
        )
    } else {
        MainScaffold(navController, authManager, calcProvider)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    authManager: ci.nsu.mobile.domain.auth.AuthManager,
    calcProvider: ci.nsu.mobile.domain.calculations.CalculationsProvider
) {
    val bottomScreens = listOf(Screen.Users, Screen.History, Screen.NewCalc)
    val currentUserId = ServiceLocator.authManager.getCurrentUserLogin()?.hashCode()?.toLong() ?: 0L

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") },
                actions = {
                    IconButton(onClick = {
                        authManager.logout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                bottomScreens.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Users.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Users.route) {
                UsersScreen(
                    viewModel = viewModel { AuthViewModel(authManager) },
                    onUserSelected = { user: UserDto ->
                        navController.navigate(Screen.Profile.createRoute(user.userId))
                    }
                )
            }

            composable(
                route = Screen.Profile.route,
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                ProfileScreen(
                    user = null, // TODO: Pass user data
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel { DepositViewModel(calcProvider, currentUserId) },
                    currentUsername = authManager.getCurrentUserLogin()
                )
            }

            composable(Screen.NewCalc.route) {
                NewCalculationScreen(
                    viewModel = viewModel { DepositViewModel(calcProvider, currentUserId) }
                )
            }
        }
    }
}