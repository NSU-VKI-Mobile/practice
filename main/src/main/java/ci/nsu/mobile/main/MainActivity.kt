package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.FirstScreenContent
import ci.nsu.mobile.main.ui.screens.MainScreenContent
import ci.nsu.mobile.main.ui.screens.SecondScreenContent
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                NavControlFun()
            }
        }
    }
}

sealed class Routes(val route: String) {
    object MainScreen : Routes("MainScreen")
    object FistScreen : Routes("FirstScreen")
    object SecondScreen : Routes("SecondScreen")
    object ResultScreen: Routes("ResultScreen")
    object HistoryScreen: Routes("HistoryScreen")
}

@Composable
fun NavControlFun() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.MainScreen.route) {
        composable(Routes.MainScreen.route) {MainScreenContent(navController)}
        composable(Routes.FistScreen.route) {FirstScreenContent(navController)}
        composable(Routes.SecondScreen.route) {SecondScreenContent(navController)}
        composable(Routes.HistoryScreen.route) {}
        composable(Routes.ResultScreen.route) {}
    }
}