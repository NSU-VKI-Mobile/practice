package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.deposit.AdditionalParamsScreen
import ci.nsu.mobile.main.ui.screens.deposit.DepositInputScreen
import ci.nsu.mobile.main.ui.screens.deposit.ResultScreen
import ci.nsu.mobile.main.ui.screens.mycalculations.MyCalculationDetailScreen
import ci.nsu.mobile.main.ui.screens.mycalculations.MyCalculationsScreen
import ci.nsu.mobile.main.ui.screens.users.UsersScreen
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.MyCalculationsViewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    depositViewModel: DepositViewModel,
    myCalculationsViewModel: MyCalculationsViewModel,
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()

    val items = listOf(
        ScreenItem("users", "Пользователи", android.R.drawable.ic_menu_manage),
        ScreenItem("my_calculations", "Мои расчёты", android.R.drawable.ic_menu_edit),
        ScreenItem("new_calculation", "Новый расчёт", android.R.drawable.ic_menu_add)
    )

    var selectedItem by remember { mutableStateOf(items[0].route) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomAppBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item.route,
                        onClick = {
                            selectedItem = item.route
                            navController.navigate(item.route) {
                                popUpTo(0) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "my_calculations",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Вкладка 1: Пользователи
            composable("users") {
                UsersScreen(
                    onLogout = onLogout,
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

            // Вкладка 3: Новый расчёт
            composable("new_calculation") {
                DepositInputScreen(
                    onBackClick = { /* Отдельная вкладка, ничего не делаем */ },
                    onNextClick = { amount, months ->
                        depositViewModel.saveFirstScreenData(amount, months)
                        navController.navigate("additional_params_from_new")
                    }
                )
            }

            // Доп. параметры для нового расчёта
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

            // Результат расчёта
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

data class ScreenItem(
    val route: String,
    val title: String,
    val iconRes: Int
)