package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.AdditionalParamsScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    var savedAmount by remember { mutableStateOf(0.0) }
    var savedMonths by remember { mutableStateOf(0) }
    var savedRate by remember { mutableStateOf(0.0) }
    var savedTopUp by remember { mutableStateOf(0.0) }

    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            MainScreen(
                onCalculateClick = {
                    navController.navigate("additional_params")
                },
                onHistoryClick = { navController.navigate("history") }
            )
        }

        composable("additional_params") {
            AdditionalParamsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateResult = { amount, months, rate, topUp ->
                    savedAmount = amount
                    savedMonths = months
                    savedRate = rate
                    savedTopUp = topUp
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ResultScreen(
                amount = savedAmount,
                months = savedMonths,
                rate = savedRate,
                topUp = savedTopUp,
                onNavigateHome = {
                    navController.popBackStack("main", inclusive = false)
                }
            )
        }

        composable("history") {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}