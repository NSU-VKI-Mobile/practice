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

        // 1. Инициализируем базу данных и репозиторий
        val database = AppDatabase.getDatabase(this)
        val repository = DepositRepository(database.depositDao())

        // 2. Создаем ViewModel, которая будет хранить данные между экранами
        val depositViewModel = DepositViewModel(repository)

        setContent {
            // Настраиваем тему оформления
            MaterialTheme {
                Surface {
                    // Создаем контроллер навигации
                    val navController = rememberNavController()

                    // Описываем все экраны приложения и правила перехода между ними
                    NavHost(navController = navController, startDestination = "home") {

                        // Главный экран
                        composable("home") {
                            HomeScreen(
                                onCalculateClick = { navController.navigate("input") },
                                onHistoryClick = { /* Здесь будет переход в историю */ },
                                onExitClick = { finish() } // Закрыть приложение
                            )
                        }

                        // Первый этап: Ввод суммы и срока
                        composable("input") {
                            DepositInputScreen(
                                amount = depositViewModel.initialAmountStr,
                                onAmountChange = { depositViewModel.initialAmountStr = it },
                                period = depositViewModel.periodMonthsStr,
                                onPeriodChange = { depositViewModel.periodMonthsStr = it },
                                onNextClick = { navController.navigate("additional") },
                                onHomeClick = { navController.popBackStack("home", false) }
                            )
                        }

                        // Второй этап: Выбор ставки и пополнение
                        composable("additional") {
                            AdditionalParamsScreen(
                                periodMonths = depositViewModel.periodMonthsStr,
                                onBackClick = { navController.popBackStack() },
                                onCalculateClick = { rate, topUp ->
                                    depositViewModel.interestRate = rate
                                    depositViewModel.monthlyTopUpStr = topUp
                                    depositViewModel.calculate() // Считаем результат
                                    navController.navigate("result")
                                }
                            )
                        }

                        // Третий экран: Итоги расчёта
                        composable("result") {
                            ResultScreen(
                                viewModel = depositViewModel,
                                onSaveClick = {
                                    depositViewModel.saveResult() // Сохраняем в БД
                                    navController.popBackStack("home", false) // Возврат на главную
                                },
                                onHomeClick = { navController.popBackStack("home", false) }
                            )
                        }
                    }
                }
            }
        }
    }
}