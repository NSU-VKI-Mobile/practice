package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.repository.DepositRepository
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.DepositViewModelFactory
import ci.nsu.mobile.main.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация Синглтона БД и Репозитория
        val database = AppDatabase.getDatabase(this)
        val repository = DepositRepository(database.depositDao())

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Создаем ViewModel
                    val viewModel: DepositViewModel = viewModel(
                        factory = DepositViewModelFactory(repository)
                    )

                    val navController = rememberNavController()

                    // Граф навигации
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(
                                onNavigateToStep1 = { navController.navigate("step1") },
                                onNavigateToHistory = { navController.navigate("history") }
                            )
                        }
                        composable("step1") {
                            Step1Screen(
                                viewModel = viewModel,
                                onNext = { navController.navigate("step2") },
                                onCancel = {
                                    navController.popBackStack("main", inclusive = false)
                                }
                            )
                        }
                        composable("step2") {
                            Step2Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onCalculate = { navController.navigate("result") }
                            )
                        }
                        composable("result") {
                            ResultScreen(
                                viewModel = viewModel,
                                onSave = { navController.popBackStack("main", inclusive = false) },
                                onGoHome = { navController.popBackStack("main", inclusive = false) }
                            )
                        }
                        composable("history") {
                            HistoryScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigateToDetail = { navController.navigate("history_detail") } // Тот самый новый переход!
                            )
                        }
                        // НОВЫЙ ЭКРАН ДЕТАЛЕЙ
                        composable("history_detail") {
                            HistoryDetailScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}