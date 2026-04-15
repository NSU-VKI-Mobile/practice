package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.additional.AdditionalScreen
import ci.nsu.mobile.main.ui.calculation.CalculationScreen
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.main.MainViewModel
import ci.nsu.mobile.main.ui.result.ResultScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenList.Main.route
    ) {
        composable(ScreenList.Main.route) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(
                viewModel = viewModel,
                onNavigateToCalculation = {
                    navController.navigate(ScreenList.Calculation.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHistory = {
                    navController.navigate(ScreenList.History.route){
                        launchSingleTop = true
                    }
                }
            )


        }

        composable(ScreenList.Calculation.route) {
            CalculationScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAdditional = { amount, period ->
                    navController.navigate(
                        ScreenList.Additional.passArguments(amount, period)
                    ){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = ScreenList.Additional.route,
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("term") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val term = backStackEntry.arguments?.getString("term") ?: ""

            AdditionalScreen(
                initialAmount = amount,
                initialTerm = term,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCalculate = { amount, term, rate, monthlyAddition ->
                    navController.navigate(
                        ScreenList.Result.passArguments(amount, term, rate, monthlyAddition)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = ScreenList.Result.route,
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("term") { type = NavType.StringType },
                navArgument("rate") { type = NavType.StringType },
                navArgument("monthlyAddition") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
            val term = backStackEntry.arguments?.getString("term")?.toIntOrNull() ?: 0
            val rate = backStackEntry.arguments?.getString("rate")?.toDoubleOrNull() ?: 0.0
            val monthlyAddition = backStackEntry.arguments?.getString("monthlyAddition")?.toDoubleOrNull() ?: 0.0

            ResultScreen(
                amount = amount,
                term = term,
                rate = rate,
                monthlyAddition = monthlyAddition,
                onSave = {
                    // TODO: сохранение в базу данных
                    navController.popBackStack(ScreenList.Main.route, inclusive = false)
                },
                onNavigateToMain = {
                    navController.popBackStack(ScreenList.Main.route, inclusive = false)
                }
            )
        }

        composable(ScreenList.History.route) {
            //TODO Здесь будет HistoryScreen
        }
    }
}