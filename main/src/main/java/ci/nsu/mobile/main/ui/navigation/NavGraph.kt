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
                    // TODO: переход на экран с результатом
                    // navController.navigate("result/$amount/$term/$rate/$monthlyAddition")
                    navController.popBackStack() // временно просто возвращаемся
                }
            )
        }

        composable(ScreenList.History.route) {
            // Здесь будет HistoryScreen
        }
    }
}