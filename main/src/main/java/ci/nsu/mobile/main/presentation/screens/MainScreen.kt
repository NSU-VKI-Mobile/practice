package ci.nsu.mobile.main.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.presentation.screens.history.HistoryDetailScreen
import ci.nsu.mobile.main.presentation.screens.history.HistoryScreen
import ci.nsu.mobile.main.presentation.screens.input.InputViewModel
import ci.nsu.mobile.main.presentation.screens.input.InputViewModelFactory
import ci.nsu.mobile.main.presentation.screens.input.Step1Screen
import ci.nsu.mobile.main.presentation.screens.input.Step2Screen
import ci.nsu.mobile.main.presentation.screens.result.ResultScreen
import ci.nsu.mobile.main.presentation.screens.users.UsersScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val application = LocalContext.current.applicationContext as DepositApplication

    var currentScreen by remember { mutableStateOf("main") }
    var detailId by remember { mutableStateOf<Long?>(null) }
    var resultData by remember { mutableStateOf<Map<String, String>?>(null) }

    val inputViewModel: InputViewModel = viewModel(
        factory = InputViewModelFactory(application.locator.calculateDepositUseCase)
    )

    Scaffold(
        topBar = {
            if (currentScreen == "main") {
                TopAppBar(title = { Text("Расчёт вкладов") })
            }
        },
        bottomBar = {
            if (currentScreen == "main") {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Пользователи") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text("Мои расчёты") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Новый расчёт") }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                detailId != null -> {
                    HistoryDetailScreen(
                        calculationId = detailId!!,
                        onBack = { detailId = null }
                    )
                }
                resultData != null -> {
                    ResultScreen(
                        initialAmount = resultData!!["initialAmount"] ?: "",
                        periodMonths = resultData!!["periodMonths"] ?: "",
                        interestRate = resultData!!["interestRate"]?.toDoubleOrNull(),
                        monthlyTopUp = resultData!!["monthlyTopUp"]
                    )
                }
                else -> {
                    when (selectedTab) {
                        0 -> UsersScreen()
                        1 -> HistoryScreen(
                            onNavigateToDetail = { id -> detailId = id }
                        )
                        2 -> Step1Screen(
                            viewModel = inputViewModel,
                            onNavigateToHome = { selectedTab = 0 },
                            onNext = { currentScreen = "step2" }
                        )
                    }
                }
            }
        }
    }

    if (currentScreen == "step2") {
        Step2Screen(
            viewModel = inputViewModel,
            onCalculate = { initialAmount, periodMonths, interestRate, monthlyTopUp ->
                resultData = mapOf(
                    "initialAmount" to initialAmount,
                    "periodMonths" to periodMonths,
                    "interestRate" to interestRate,
                    "monthlyTopUp" to monthlyTopUp
                )
                currentScreen = "result"
            },
            onBack = { currentScreen = "main" }
        )
    }
}