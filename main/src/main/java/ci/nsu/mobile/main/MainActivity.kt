package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.navigation.Routes
import ci.nsu.mobile.main.ui.screens.FirstScreenContent
import ci.nsu.mobile.main.ui.screens.HistoryScreenContent
import ci.nsu.mobile.main.ui.screens.MainScreenContent
import ci.nsu.mobile.main.ui.screens.ResultScreenContent
import ci.nsu.mobile.main.ui.screens.SecondScreenContent
import ci.nsu.mobile.main.ui.theme.AppTheme
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var repository: DepositRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)
        repository = DepositRepository(database.depositDao())
        setContent {
            AppTheme {
                NavControlFun(repository)
            }
        }
    }
}

@Composable
fun NavControlFun(repository: DepositRepository) {
    val navController = rememberNavController()
    val viewModel = remember { DepositCalculationViewModel(repository) }
    NavHost(navController, startDestination = Routes.MainScreen.route) {
        composable(Routes.MainScreen.route) {MainScreenContent(navController)}
        composable(Routes.FirstScreen.route) {FirstScreenContent(navController, viewModel)}
        composable(Routes.SecondScreen.route) {SecondScreenContent(navController, viewModel)}
        composable(Routes.HistoryScreen.route) { HistoryScreenContent(navController, viewModel) }
        composable(Routes.ResultScreen.route) { ResultScreenContent(navController, viewModel) }
    }
}