package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import ci.nsu.moble.main.di.ServiceLocator
import ci.nsu.moble.main.data.models.DepositCalculation
import ci.nsu.moble.main.viewmodel.AuthViewModel

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
    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf(0) }
    var newCalculationKey by remember { mutableStateOf(System.currentTimeMillis()) }

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
                        navController.navigate("users")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Мои расчёты") },
                    label = { Text("Мои расчёты") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("my_calculations")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Новый расчёт") },
                    label = { Text("Новый расчёт") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        // генерируем новый ключ при каждом нажатии
                        newCalculationKey = System.currentTimeMillis()
                        navController.navigate("new_calculation/$newCalculationKey") {
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
            composable("users") {
                MainScreen(
                    onLogout = onLogout,
                    viewModel = authViewModel
                )
            }

            composable("my_calculations") {
                var refreshKey by remember { mutableStateOf(0L) }
                LaunchedEffect(Unit) {
                    refreshKey = System.currentTimeMillis()
                }
                key(refreshKey) {
                    MyCalculationsScreen(
                        userId = userId,
                        repository = depositRepository,
                        onItemClick = { calculation ->
                            navController.navigate("calculation_detail/${calculation.id}")
                        }
                    )
                }
            }

            composable(
                route = "new_calculation/{key}",
                arguments = listOf(
                    navArgument("key") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val key = backStackEntry.arguments?.getLong("key") ?: newCalculationKey
                // key заставляет экран пересоздаваться при каждом изменении
                androidx.compose.runtime.key(key) {
                    NewCalculationScreen(
                        userId = userId,
                        repository = depositRepository,
                        onSaveSuccess = {
                            selectedItem = 1
                            navController.navigate("my_calculations") {
                                popUpTo("my_calculations") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(
                route = "calculation_detail/{calculationId}",
                arguments = listOf(
                    navArgument("calculationId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L
                var calculation by remember { mutableStateOf<DepositCalculation?>(null) }

                LaunchedEffect(calculationId) {
                    calculation = depositRepository.getCalculationById(calculationId)
                }

                if (calculation != null) {
                    CalculationDetailScreen(
                        calculation = calculation!!,
                        onBack = { navController.popBackStack() },
                        onDelete = {
                            scope.launch {
                                depositRepository.deleteCalculation(calculation!!)
                                navController.popBackStack()
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}