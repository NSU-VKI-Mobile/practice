package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.HomeScreen
import ci.nsu.mobile.main.ui.screens.DepositInputScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    DepositApp(onExit = this::finish)
                }
            }
        }
    }
}

@Composable
fun DepositApp(onExit: () -> Unit) {
    val navController = rememberNavController()
    val viewModel: DepositViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Главный экран
        composable("home") {
            HomeScreen(
                onCalculateClick = {
                    navController.navigate("deposit_input")
                },
                onHistoryClick = {
                    // TODO: Добавить экран истории...
                },
                onExitClick = onExit
            )
        }

        // Экран ввода параметров
        composable("deposit_input") {
            DepositInputScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = { amount, months ->
                    viewModel.saveFirstScreenData(amount, months)
                    navController.navigate("result")
                }
            )
        }

        // Экран результата
        composable("result") {
            ResultScreen(
                onSaveClick = {
                    // TODO: Добавить сохранение данных в бд
                    navController.popBackStack("home", inclusive = false)
                },
                onBackToHomeClick = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}