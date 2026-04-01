package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.local.TokenManager

// Импорты экранов
import ci.nsu.mobile.main.ui.LoginScreen
import ci.nsu.mobile.main.ui.RegisterScreen
import ci.nsu.mobile.main.ui.UsersScreen
import ci.nsu.mobile.main.ui.DepositCalculatorScreen
import ci.nsu.mobile.main.ui.HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// ================= ГЛОБАЛЬНАЯ НАВИГАЦИЯ =================
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Проверяем наличие токена для выбора стартового экрана (Вход или Главный)
    val startDest = if (TokenManager.token.isNullOrEmpty()) "login" else "main"

    NavHost(navController = navController, startDestination = startDest) {

        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true } // Удаляем логин из истории
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
            MainScreenWithBottomNav(
                onLogout = {
                    TokenManager.clear()
                    navController.navigate("login") {
                        popUpTo(0) // Полностью очищаем стек навигации при выходе
                    }
                }
            )
        }
    }
}

// ================= ЛОКАЛЬНАЯ НАВИГАЦИЯ (НИЖНЕЕ МЕНЮ) =================
@Composable
fun MainScreenWithBottomNav(onLogout: () -> Unit) {
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
                    icon = { Icon(Icons.Default.List, contentDescription = "Мои расчёты") },
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
                // Оборачиваем UsersScreen, чтобы добавить нашу красную кнопку выхода сверху
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
                DepositCalculatorScreen()
            }

            composable("history") {
                HistoryScreen()
            }
        }
    }
}