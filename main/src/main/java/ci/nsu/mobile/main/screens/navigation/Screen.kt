// ui/navigation/Navigation.kt
package ci.nsu.mobile.main.screens.navigation

import android.os.Process
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.screens.DetailScreen
import ci.nsu.mobile.main.screens.HistoryScreen
import ci.nsu.mobile.main.screens.MainScreen
import ci.nsu.mobile.main.screens.ResultScreen
import ci.nsu.mobile.main.screens.StepOneScreen
import ci.nsu.mobile.main.screens.StepTwoScreen

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object StepOne : Screen("step_one")
    object StepTwo : Screen("step_two/{initialAmount}/{periodMonths}") {
        fun createRoute(initialAmount: Double, periodMonths: Int) =
            "step_two/$initialAmount/$periodMonths"
    }
    object Result : Screen("result/{initialAmount}/{periodMonths}/{interestRate}/{monthlyTopUp}") {
        fun createRoute(
            initialAmount: Double,
            periodMonths: Int,
            interestRate: Double,
            monthlyTopUp: Double?
        ) = "result/$initialAmount/$periodMonths/$interestRate/${monthlyTopUp ?: "null"}"
    }
    object History : Screen("history")
    object Detail : Screen("detail/{calculationId}") {
        fun createRoute(calculationId: Long) = "detail/$calculationId"
    }
}

@Composable
fun AppNavigation(repository: DepositRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(
                onCalculate = { navController.navigate(Screen.StepOne.route) },
                onHistory = { navController.navigate(Screen.History.route) },
                onExit = { Process.killProcess(Process.myPid()) }
            )
        }

        composable(Screen.StepOne.route) {
            StepOneScreen(
                onBack = { navController.popBackStack() },
                onNext = { initialAmount, periodMonths ->
                    navController.navigate(
                        Screen.StepTwo.createRoute(initialAmount, periodMonths)
                    )
                }
            )
        }

        composable(
            route = Screen.StepTwo.route,
            arguments = listOf(
                navArgument("initialAmount") { type = NavType.FloatType },
                navArgument("periodMonths") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val initialAmount = backStackEntry.arguments?.getDouble("initialAmount") ?: 0.0
            val periodMonths = backStackEntry.arguments?.getInt("periodMonths") ?: 0

            StepTwoScreen(
                periodMonths = periodMonths,
                initialAmount = initialAmount,
                onBack = { navController.popBackStack() },
                onCalculate = { initAmount, monthlyTopUp, rate, months ->
                    navController.navigate(
                        Screen.Result.createRoute(initAmount, months.toInt(), rate, monthlyTopUp)
                    )
                }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("initialAmount") { type = NavType.FloatType },
                navArgument("periodMonths") { type = NavType.IntType },
                navArgument("interestRate") { type = NavType.FloatType },
                navArgument("monthlyTopUp") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val initialAmount = backStackEntry.arguments?.getDouble("initialAmount") ?: 0.0
            val periodMonths = backStackEntry.arguments?.getInt("periodMonths") ?: 0
            val interestRate = backStackEntry.arguments?.getDouble("interestRate") ?: 0.0
            val monthlyTopUpStr = backStackEntry.arguments?.getString("monthlyTopUp")
            val monthlyTopUp = if (monthlyTopUpStr == "null") null else monthlyTopUpStr?.toDoubleOrNull()

            ResultScreen(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                repository = repository,
                onBack = {
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                },
                onSave = {}
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onItemClick = { calculationId ->
                    navController.navigate(Screen.Detail.createRoute(calculationId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("calculationId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L

            DetailScreen(
                calculationId = calculationId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}