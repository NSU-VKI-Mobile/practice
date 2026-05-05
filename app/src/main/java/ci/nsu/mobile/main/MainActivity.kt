package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.auth.ui.LoginScreen
import ci.nsu.mobile.auth.ui.RegisterScreen
import ci.nsu.mobile.auth.ui.UsersScreen
import ci.nsu.mobile.calculations.ui.DepositCalculatorScreen
import ci.nsu.mobile.calculations.ui.HistoryScreen
import ci.nsu.mobile.domain.auth.AuthManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(authManager = authManager)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(authManager: AuthManager) {
    val navController = rememberNavController()
    val startDestination = if (authManager.isLoggedIn()) "main" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            val userId = authManager.getCurrentUser()?.id ?: 0L
            MainScreenWithBottomNav(
                userId = userId,
                onLogout = {
                    authManager.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreenWithBottomNav(
    userId: Long,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Пользователи") },
                    label = { Text("Пользователи") },
                    selected = currentRoute == "users",
                    onClick = {
                        bottomNavController.navigate("users") {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Новый расчёт") },
                    label = { Text("Новый расчёт") },
                    selected = currentRoute == "new_calc",
                    onClick = {
                        bottomNavController.navigate("new_calc") {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Мои расчёты") },
                    label = { Text("Мои расчёты") },
                    selected = currentRoute == "history",
                    onClick = {
                        bottomNavController.navigate("history") {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = "users",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("users") {
                Column(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Выйти из аккаунта")
                    }
                    UsersScreen()
                }
            }

            composable("new_calc") {
                DepositCalculatorScreen(userId = userId)
            }

            composable("history") {
                HistoryScreen(userId = userId)
            }
        }
    }
}
