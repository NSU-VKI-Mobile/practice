package com.example.app.navigation

import UsersScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app.ui.screens.HistoryScreenContent
import com.example.app.viewmodel.historyDeposits.HistoryDepositsViewModel
import com.example.app.viewmodel.users.UsersViewModel
import com.example.domain.interfaces.AuthManager
import com.example.domain.interfaces.AuthNavigator
import com.example.domain.interfaces.CalculationsNavigator

sealed class Screens(val route: String) {
    object UsersScreen : Screens("UsersScreen")
    object HistoryScreen : Screens("HistoryScreen")
    object MainScreen : Screens("MainScreen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
    navController: NavHostController,
    usersViewModel: UsersViewModel,
    historyDepositsViewModel: HistoryDepositsViewModel,
    authManager: AuthManager,
    authNavigator: AuthNavigator,
    calculationsNavigator: CalculationsNavigator
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ✅ Проверка авторизации при старте
    LaunchedEffect(Unit) {
        if (!authManager.isLoggedIn()) {
            authNavigator.navigateToLogin(navController.context)
        }
    }

    val showBottomBar = currentRoute in listOf(
        Screens.UsersScreen.route,
        Screens.HistoryScreen.route,
        Screens.MainScreen.route
    )

    val showLogOut = currentRoute == Screens.UsersScreen.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.People, contentDescription = "Пользователи") },
                        label = { Text("Пользователи") },
                        selected = currentRoute == Screens.UsersScreen.route,
                        onClick = {
                            if (currentRoute != Screens.UsersScreen.route) {
                                navController.navigate(Screens.UsersScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Money, contentDescription = "Мои расчеты") },
                        label = { Text("Мои расчеты") },
                        selected = currentRoute == Screens.HistoryScreen.route,
                        onClick = {
                            if (currentRoute != Screens.HistoryScreen.route) {
                                navController.navigate(Screens.HistoryScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.AddCard, contentDescription = "Новый расчет") },
                        label = { Text("Новый расчет") },
                        selected = currentRoute == Screens.MainScreen.route,
                        onClick = {
                            val userId = authManager.getCurrentUser()?.userId ?: -1L
                            if (userId != -1L) {
                                calculationsNavigator.navigateToNewCalculation(
                                    navController.context,
                                    userId
                                )
                            }
                        }
                    )
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("РАСЧЕТ ВКЛАДОВ") },
                actions = {
                    if (showLogOut) {
                        IconButton(
                            onClick = {
                                authManager.logout()
                                authNavigator.navigateToLogin(navController.context)
                                navController.navigate(Screens.UsersScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = "Выйти"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.UsersScreen.route,  // ✅ оставляем, но LaunchedEffect сразу перенаправит
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.UsersScreen.route) {
                UsersScreen(
                    viewModel = usersViewModel
                )
            }
            composable(Screens.HistoryScreen.route) {
                HistoryScreenContent(
                    viewModel = historyDepositsViewModel
                )
            }
            composable(Screens.MainScreen.route) {
                Text("Новый расчет")
            }
        }
    }
}