package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.di.ServiceLocator
import ci.nsu.moble.main.viewmodel.AuthViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun MainTabsScreen(
    userId: Long,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val serviceLocator = remember { ServiceLocator(context) }
    val depositRepository = serviceLocator.depositRepository
    val authRepository = serviceLocator.authRepository
    val navController = rememberNavController()

    var selectedItem by remember { mutableStateOf(0) }

    // временная заглушка для AuthViewModel
    val authViewModel = AuthViewModel(authRepository)

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Пользователи") },
                    label = { Text("Пользователи") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("users") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Мои расчёты") },
                    label = { Text("Мои расчёты") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("my_calculations") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Новый расчёт") },
                    label = { Text("Новый расчёт") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("new_calculation") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "users",
            modifier = Modifier.padding(innerPadding)
        ) {
            // экран пользователей (у тебя уже есть UsersScreen или MainScreen)
            composable("users") {
                MainScreen(
                    onLogout = onLogout,
                    viewModel = authViewModel
                )
            }

            // мои расчёты
            composable("my_calculations") {
                MyCalculationsScreen(
                    userId = userId,
                    repository = depositRepository,
                    onItemClick = { calculation ->
                        // переход на детали расчёта
                        navController.navigate("calculation_detail/${calculation.id}")
                    }
                )
            }

            // новый расчёт
            composable("new_calculation") {
                NewCalculationScreen(
                    userId = userId,
                    repository = depositRepository,
                    onSaveSuccess = {
                        // после сохранения переключаемся на список расчётов
                        selectedItem = 1
                        navController.navigate("my_calculations") {
                            popUpTo("my_calculations") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // детали расчёта (с параметром id)
            composable(
                route = "calculation_detail/{calculationId}",
                arguments = listOf(
                    navArgument("calculationId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L
                // TODO: получить расчёт по id из базы
                // пока заглушка
                Text("Детали расчёта #$calculationId")
            }
        }
    }
}