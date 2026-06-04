package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.calculations.ui.screens.deposit.DepositInputScreen
import ci.nsu.mobile.calculations.ui.screens.deposit.AdditionalParamsScreen
import ci.nsu.mobile.calculations.ui.screens.deposit.ResultScreen
import ci.nsu.mobile.calculations.ui.screens.mycalculations.MyCalculationsScreen
import ci.nsu.mobile.calculations.ui.screens.mycalculations.MyCalculationDetailScreen
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.calculations.viewmodel.MyCalculationsViewModel
import ci.nsu.mobile.main.ui.screens.users.UsersScreen
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import androidx.compose.runtime.collectAsState
import ci.nsu.mobile.auth.di.AuthNavigatorImpl
import ci.nsu.mobile.calculations.di.CalculationsNavigatorImpl

@Composable
fun MainScreen(
    usersViewModel: UsersViewModel,
    depositViewModel: DepositViewModel,
    myCalculationsViewModel: MyCalculationsViewModel
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val authNavigator = AuthNavigatorImpl(navController)
    val calculationsNavigator = CalculationsNavigatorImpl(navController)

    val items = listOf(
        "Пользователи" to android.R.drawable.ic_menu_manage,
        "Мои расчёты" to android.R.drawable.ic_menu_edit,
        "Новый расчёт" to android.R.drawable.ic_menu_add
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                items.forEachIndexed { index, (title, icon) ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                0 -> navController.navigate("users")
                                1 -> calculationsNavigator.navigateToMyCalculations()
                                2 -> calculationsNavigator.navigateToNewCalculation()
                            }
                        },
                        icon = { Icon(painterResource(id = icon), contentDescription = title) },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "users",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Вкладка 1: Пользователи
            composable("users") {
                UsersScreen(
                    onLogout = { authNavigator.navigateToLogin() },
                    viewModel = usersViewModel
                )
            }

            // Вкладка 2: Мои расчёты
            composable("my_calculations") {
                val calculations by myCalculationsViewModel.calculations.collectAsState()
                val isLoading by myCalculationsViewModel.isLoading.collectAsState()
                val error by myCalculationsViewModel.error.collectAsState()

                MyCalculationsScreen(
                    calculations = calculations,
                    isLoading = isLoading,
                    error = error,
                    onItemClick = { id ->
                        navController.navigate("my_calculation_detail/$id")
                    },
                    onRefresh = { myCalculationsViewModel.loadCalculations() }
                )
            }

            // Вкладка 3: Новый расчёт (шаг 1)
            composable("new_calculation") {
                DepositInputScreen(
                    onBackClick = { /* Ничего не делаем */ },
                    onNextClick = { amount, months ->
                        depositViewModel.saveFirstScreenData(amount, months)
                        navController.navigate("additional_params_from_new")
                    }
                )
            }

            // Шаг 2 нового расчёта
            composable("additional_params_from_new") {
                AdditionalParamsScreen(
                    periodMonths = depositViewModel.getPeriodMonths(),
                    onBackClick = { navController.popBackStack() },
                    onCalculateClick = { rate, topUp ->
                        depositViewModel.saveSecondScreenData(rate, topUp)
                        navController.navigate("result_from_new")
                    }
                )
            }

            // Шаг 3 нового расчёта (результат)
            composable("result_from_new") {
                ResultScreen(
                    initialAmount = depositViewModel.getInitialAmount(),
                    periodMonths = depositViewModel.getPeriodMonths(),
                    interestRate = depositViewModel.getInterestRate(),
                    monthlyTopUp = depositViewModel.getMonthlyTopUp(),
                    finalAmount = depositViewModel.getFinalAmount(),
                    interestEarned = depositViewModel.getInterestEarned(),
                    onSaveClick = {
                        depositViewModel.saveCalculation()
                        navController.popBackStack("new_calculation", inclusive = false)
                    },
                    onBackToHomeClick = {
                        navController.popBackStack("new_calculation", inclusive = false)
                    }
                )
            }

            // Детали расчёта (из Мои расчёты)
            composable("my_calculation_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
                val calculations by myCalculationsViewModel.calculations.collectAsState()
                val calculation = calculations.find { it.id == id }
                val deletingId by myCalculationsViewModel.deletingId.collectAsState()

                MyCalculationDetailScreen(
                    calculation = calculation,
                    isDeleting = deletingId == id,
                    onDelete = {
                        myCalculationsViewModel.deleteCalculation(id)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}