package com.example.integratedapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.integratedapp.ui.calculations.CalculationsScreen
import com.example.integratedapp.ui.newcalc.NewCalcScreen
import com.example.integratedapp.ui.users.UsersScreen

// Маршруты нижней навигации
sealed class BottomScreen(val route: String, val label: String) {
    object Users        : BottomScreen("bottom_users", "Пользователи")
    object Calculations : BottomScreen("bottom_calcs", "Мои расчёты")
    object NewCalc      : BottomScreen("bottom_new",   "Новый расчёт")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Расчёт вкладов", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        // NavHost для переключения между тремя вкладками
        NavHost(
            navController = navController,
            startDestination = BottomScreen.Users.route,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            composable(BottomScreen.Users.route) { UsersScreen() }
            composable(BottomScreen.Calculations.route) { CalculationsScreen() }
            composable(BottomScreen.NewCalc.route) { NewCalcScreen() }
        }
    }
}

@Composable
private fun BottomNav(navController: NavHostController) {
    val items = listOf(BottomScreen.Users, BottomScreen.Calculations, BottomScreen.NewCalc)

    NavigationBar {
        // currentBackStackEntry — текущий экран
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            BottomScreen.Users -> Icons.Default.AccountCircle
                            BottomScreen.Calculations -> Icons.Default.List
                            BottomScreen.NewCalc -> Icons.Default.Add
                        },
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) }
            )
        }
    }
}
