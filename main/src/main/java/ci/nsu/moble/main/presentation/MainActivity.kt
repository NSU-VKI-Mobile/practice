package ci.nsu.moble.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.presentation.screens.*
import ci.nsu.moble.main.presentation.viewmodels.CalculationViewModel
import ci.nsu.moble.main.presentation.screens.DetailScreen
import ci.nsu.moble.main.presentation.screens.HistoryScreen
import ci.nsu.moble.main.presentation.screens.MainScreen
import ci.nsu.moble.main.presentation.screens.Step1Screen
import ci.nsu.moble.main.presentation.screens.Step2Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val calcVM: CalculationViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            onCalculate = { navController.navigate("step1") },
                            onHistory = { navController.navigate("history") },
                            onClose = { finish() }
                        )
                    }
                    composable("step1") {
                        Step1Screen(
                            calcVM = calcVM,
                            onBack = { navController.popBackStack("main", false) },
                            onNext = { navController.navigate("step2") }
                        )
                    }
                    composable("step2") {
                        Step2Screen(
                            calcVM = calcVM,
                            onBack = { navController.popBackStack() },
                            onCalculate = { navController.navigate("result") }
                        )
                    }
                    composable("result") {
                        ResultScreen(
                            calcVM = calcVM,
                            onSave = { navController.popBackStack("main", false) },
                            onBackToMain = { navController.popBackStack("main", false) }
                        )
                    }
                    composable("history") {
                        HistoryScreen(
                            onItemClick = { id -> navController.navigate("detail/$id") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("detail/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: -1
                        DetailScreen(
                            id = id,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}