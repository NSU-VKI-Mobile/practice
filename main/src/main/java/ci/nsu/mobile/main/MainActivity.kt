package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    private lateinit var repos: DepositRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)
        repos = DepositRepository.getInstance(database.depositDao())
        setContent {
            PracticeTheme {
                NavControlFun()
            }
        }
    }
}

@Composable
fun NavControlFun() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.MainScreen.route) {
        composable(Routes.MainScreen.route) {MainScreenContent(navController)}
        composable(Routes.FistScreen.route) {FirstScreenContent(navController)}
        composable(Routes.SecondScreen.route) {SecondScreenContent(navController)}
        composable(Routes.HistoryScreen.route) { HistoryScreenContent((navController)) }
        composable(Routes.ResultScreen.route) { ResultScreenContent(navController) }
    }
}