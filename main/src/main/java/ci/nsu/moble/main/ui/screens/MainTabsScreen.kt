package ci.nsu.moble.main.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.getGraphViewModel
import ci.nsu.moble.main.ui.screens.main.HistoryScreen
import ci.nsu.moble.main.ui.screens.main.MainScreen
import ci.nsu.moble.main.ui.screens.main.calc.ResultScreen
import ci.nsu.moble.main.ui.screens.main.calc.Step1Screen
import ci.nsu.moble.main.ui.screens.main.calc.Step2Screen
import ci.nsu.moble.main.viewmodel.CalcScreensViewModel


@Composable
fun MainTabsScreen(
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Вкладка 1: Пользователи
                NavigationBarItem(
                    selected = currentRoute == "users_list",
                    onClick = {
                        bottomNavController.navigate("users_list") {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Пользователи") },
                    icon = { Icon(Icons.Default.People, contentDescription = "Пользователи") },
                )

                // Вкладка 2: История
                NavigationBarItem(
                    selected = currentRoute == "history",
                    onClick = {
                        bottomNavController.navigate("history") {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("История") },
                    icon = { Icon(Icons.Default.History, contentDescription = "История") },
                )

                // Вкладка 3: Расчет
                val isCalculationActive = currentRoute == "step1" ||
                        currentRoute == "step2" ||
                        currentRoute == "result"

                NavigationBarItem(
                    selected = isCalculationActive,
                    onClick = {
                        bottomNavController.navigate("calculation_graph") {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Расчет") },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Расчет") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "users_list",
            modifier = Modifier
        ) {
            // Экран списка пользователей
            composable("users_list") {
                MainScreen(
                    onLogout = onLogout,
                    bottomPadding = innerPadding.calculateBottomPadding()
                )
            }

            // Экран истории
            composable("history") {
                HistoryScreen(
                    bottomPadding = innerPadding.calculateBottomPadding()
                )
            }

            // Линейный подграф расчета (Этап 1 -> Этап 2 -> Результат)
            navigation(startDestination = "step1", route = "calculation_graph") {

                composable("step1") { entry ->
                    val depositVm: CalcScreensViewModel = bottomNavController.getGraphViewModel(
                        route = "calculation_graph",
                        currentEntry = entry
                    )

                    Step1Screen(depositVm, goToStep2 = { bottomNavController.navigate("step2") }, bottomPadding = innerPadding.calculateBottomPadding())
                }

                composable("step2") { entry ->
                    val depositVm: CalcScreensViewModel = bottomNavController.getGraphViewModel(
                        route = "calculation_graph",
                        currentEntry = entry
                    )

                    Step2Screen(depositVm, onBack = { bottomNavController.popBackStack() }, goToResult = { bottomNavController.navigate("result") }, bottomPadding = innerPadding.calculateBottomPadding())
                }

                composable("result") { entry ->
                    val depositVm: CalcScreensViewModel = bottomNavController.getGraphViewModel(
                        route = "calculation_graph",
                        currentEntry = entry
                    )

                    ResultScreen(depositVm,
                        goToStart = {
                            depositVm.clearFields()
                            bottomNavController.navigate("step1")
                        },
                        goToResults = {
                            // Because of inclusive, Scoped lifetime ends for screens and viewmodel will be deleted
                            bottomNavController.navigate("history") {
                                popUpTo("calculation_graph") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}