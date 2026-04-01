package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.local.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

// Глобальная навигация (Логин / Регистрация / Главный экран)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // проверяем токен сразу при запуске
    val startDest = if (TokenManager.token.isNullOrEmpty()) "login" else "main"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            // TODO: LoginScreen
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Экран входа (скоро будет)")
            }
        }
        composable("register") {
            // TODO: RegisterScreen
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Экран регистрации (скоро будет)")
            }
        }
        composable("main") {
            // новый Главный экран
            MainScreenWithBottomNav(
                onLogout = {
                    TokenManager.clear()
                    navController.navigate("login") {
                        popUpTo(0) // Сбрасываем историю экранов, чтобы нельзя было вернуться назад кнопкой "Назад"
                    }
                }
            )
        }
    }
}

// Локальная навигация по вкладкам
@Composable
fun MainScreenWithBottomNav(onLogout: () -> Unit) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Следим за текущей вкладкой, чтобы подсвечивать нужную иконку
                val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Юзеры") },
                    label = { Text("Юзеры") },
                    selected = currentRoute == "users",
                    onClick = { bottomNavController.navigate("users") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Новый") },
                    label = { Text("Новый расчёт") },
                    selected = currentRoute == "new_calc",
                    onClick = { bottomNavController.navigate("new_calc") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Мои расчёты") },
                    label = { Text("Мои расчёты") },
                    selected = currentRoute == "history",
                    onClick = { bottomNavController.navigate("history") }
                )
            }
        }
    ) { paddingValues ->
        // Навигатор внутри вкладок
        NavHost(
            navController = bottomNavController,
            startDestination = "users",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("users") {
                // TODO: список пользователей
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Список пользователей (API)") }
            }
            composable("new_calc") {
                // TODO:  калькулятор
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Калькулятор вкладов (Room)") }
            }
            composable("history") {
                // TODO: история
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("История расчетов (Room)") }
            }
        }
    }
}