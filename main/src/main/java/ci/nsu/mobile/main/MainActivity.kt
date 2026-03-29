package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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