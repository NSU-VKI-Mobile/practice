package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.navigation.DepositRoutes
import ci.nsu.mobile.main.repository.DepositRepository
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                val db = remember { AppDatabase.getDatabase(this) }
                val repository = remember { DepositRepository(db) }
                val factory = remember { DepositViewModelFactory(repository) }
                val viewModel: DepositViewModel = viewModel(factory = factory)

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = DepositRoutes.Main.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Главный экран
                        composable(DepositRoutes.Main.route) {
                            MainScreen(
                                onCalculateClick = {
                                    viewModel.reset()
                                    navController.navigate(DepositRoutes.FirstStep.route)
                                },
                                onHistoryClick = {
                                    navController.navigate(DepositRoutes.History.route)
                                },
                                onExitClick = {
                                    finish()
                                }
                            )
                        }

                        // Первый этап
                        composable(DepositRoutes.FirstStep.route) {
                            FirstStepScreen(
                                onBackToMain = {
                                    navController.popBackStack(
                                        DepositRoutes.Main.route,
                                        inclusive = false
                                    )
                                },
                                onNext = {
                                    navController.navigate(DepositRoutes.SecondStep.route)
                                }
                            )
                        }

                        // Второй этап
                        composable(DepositRoutes.SecondStep.route) {
                            SecondStepScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onCalculate = {
                                    navController.navigate(DepositRoutes.Result.route)
                                }
                            )
                        }

                        // Результат
                        composable(DepositRoutes.Result.route) {
                            ResultScreen(
                                onSave = {
                                    navController.popBackStack(
                                        DepositRoutes.Main.route,
                                        inclusive = false
                                    )
                                },
                                onBackToMain = {
                                    navController.popBackStack(
                                        DepositRoutes.Main.route,
                                        inclusive = false
                                    )
                                }
                            )
                        }

                        // История
                        composable(DepositRoutes.History.route) {
                            HistoryScreen(
                                onItemClick = { id ->
                                    navController.navigate(DepositRoutes.HistoryDetail.passId(id))
                                },
                                onBackToMain = {
                                    navController.popBackStack(
                                        DepositRoutes.Main.route,
                                        inclusive = false
                                    )
                                }
                            )
                        }

                        // Детали истории
                        composable(
                            route = DepositRoutes.HistoryDetail.route,
                            arguments = DepositRoutes.HistoryDetail.arguments
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong(DepositRoutes.HistoryDetail.ID_ARG) ?: 0L
                            HistoryDetailScreen(
                                calculationId = id,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}