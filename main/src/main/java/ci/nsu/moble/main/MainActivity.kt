package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            onCalculate = { navController.navigate("step1") },
                            onHistory = { navController.navigate("history") },
                            onClose = { finish() }
                        )
                    }

                    composable("step1") {
                        Step1Screen(
                            onBack = { navController.popBackStack("main", false) },
                            onNext = { amount, months ->
                                navController.navigate("step2/$months/$amount")
                            }
                        )
                    }

                    composable(
                        route = "step2/{months}/{amount}",
                        arguments = listOf(
                            navArgument("months") { type = NavType.IntType },
                            navArgument("amount") { type = NavType.StringType }  // Double -> String
                        )
                    ) { backStackEntry ->
                        val months = backStackEntry.arguments?.getInt("months") ?: 0
                        val amountStr = backStackEntry.arguments?.getString("amount") ?: "0.0"
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        Step2Screen(
                            periodMonths = months,
                            onBack = { navController.popBackStack() },
                            onCalculate = { rate, topUp ->
                                navController.navigate("result/$amount/$months/$rate/${topUp ?: "null"}")
                            }
                        )
                    }

                    composable(
                        route = "result/{amount}/{months}/{rate}/{topUp}",
                        arguments = listOf(
                            navArgument("amount") { type = NavType.StringType },
                            navArgument("months") { type = NavType.IntType },
                            navArgument("rate") { type = NavType.StringType },
                            navArgument("topUp") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val amountStr = backStackEntry.arguments?.getString("amount") ?: "0.0"
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        val months = backStackEntry.arguments?.getInt("months") ?: 0
                        val rateStr = backStackEntry.arguments?.getString("rate") ?: "0.0"
                        val rate = rateStr.toDoubleOrNull() ?: 0.0
                        val topUpStr = backStackEntry.arguments?.getString("topUp")
                        val topUp = if (topUpStr == "null") null else topUpStr?.toDoubleOrNull()

                        ResultScreen(
                            initialAmount = amount,
                            periodMonths = months,
                            interestRate = rate,
                            monthlyTopUp = topUp,
                            onSave = { navController.popBackStack("main", false) },
                            onBackToMain = { navController.popBackStack("main", false) }
                        )
                    }

                    composable("history") {
                        HistoryScreen(
                            onItemClick = { id -> navController.navigate("detail/$id") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: -1
                        DetailScreen(
                            id = id,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}