package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.ui.*
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = DepositRepository(database.depositDao())
        val depositViewModel = DepositViewModel(repository)

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onCalculateClick = { navController.navigate("input") },
                                onHistoryClick = { navController.navigate("history") },
                                onExitClick = { finish() }
                            )
                        }

                        composable("input") {
                            DepositInputScreen(
                                amount = depositViewModel.initialAmount.value,
                                onAmountChange = {
                                    depositViewModel.saveFirstScreenData(it, depositViewModel.periodMonths.value)
                                },
                                period = depositViewModel.periodMonths.value,
                                onPeriodChange = {
                                    depositViewModel.saveFirstScreenData(depositViewModel.initialAmount.value, it)
                                },
                                onNextClick = { navController.navigate("additional") },
                                onHomeClick = { navController.popBackStack("home", false) }
                            )
                        }

                        composable("additional") {
                            AdditionalParamsScreen(
                                periodMonths = depositViewModel.periodMonths.value,
                                onBackClick = { navController.popBackStack() },
                                onCalculateClick = { rate, topUp ->
                                    depositViewModel.saveSecondScreenData(rate, topUp)
                                    navController.navigate("result")
                                }
                            )
                        }

                        composable("result") {
                            ResultScreen(
                                viewModel = depositViewModel,
                                onSaveClick = {
                                    depositViewModel.saveCalculation()
                                    navController.popBackStack("home", false)
                                },
                                onHomeClick = { navController.popBackStack("home", false) }
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                viewModel = depositViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}