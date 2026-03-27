package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.CalcScreen
import ci.nsu.mobile.main.ui.screens.HistoryCalcScreen
import ci.nsu.mobile.main.ui.screens.Input1Screen
import ci.nsu.mobile.main.ui.screens.Input2Screen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
    object HistoryCalc : Screen("HistoryCalc")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onCalculateClick = { navController.navigate(Screen.Input1.route) },
                onHistoryClick = { navController.navigate(Screen.HistoryCalc.route) },
                onCloseClick = {(context as? Activity)?.finish()}
            )
        }

        composable(Screen.Input1.route) {
            Input1Screen(
                onBackClick = { navController.popBackStack(Screen.Main.route, inclusive = false) },
                onNextClick = {navController.navigate(Screen.Input2.route) }
            )
        }

        composable(Screen.Input2.route) {
            Input2Screen(
                onBackClick = { navController.popBackStack() },
                onCalcClick = { navController.navigate(Screen.Calc.route) }
            )
        }

        composable(Screen.Calc.route) {
            CalcScreen(
                onSaveClick = { /* сохранение в Room */ },
                onMainClick = { navController.popBackStack(Screen.Main.route, inclusive = false) }
            )
        }

        composable(Screen.HistoryCalc.route) {
            HistoryCalcScreen(
                onBackClick = { navController.popBackStack(Screen.Main.route, inclusive = false)}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting("Android")
    }
}