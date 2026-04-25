package ci.nsu.mobile.main.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.calculation.result.ResultRoute
import ci.nsu.mobile.main.ui.calculation.step1.Step1Route
import ci.nsu.mobile.main.ui.calculation.step2.Step2Route
import ci.nsu.mobile.main.ui.history.HistoryScreen
import ci.nsu.mobile.main.ui.main.MainScreen

@Composable
fun AppNavHost(activity: Activity, modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {

        composable(Screen.Main.route) {
            MainScreen(
                onCalculate = { navController.navigate(Screen.Calculation.route) },
                onHistory = { navController.navigate(Screen.History.route) },
                onExit = { activity.finish() },
            )
        }
        navigation(
            route = Screen.Calculation.route,
            startDestination = Screen.Step1.route
        ) {

            composable(Screen.Step1.route) { entry ->
                Step1Route(navController, entry)
            }

            composable(Screen.Step2.route) { entry ->
                Step2Route(navController, entry)
            }

            composable(Screen.Result.route) { entry ->
                ResultRoute(navController, entry)
            }
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }


    }
}