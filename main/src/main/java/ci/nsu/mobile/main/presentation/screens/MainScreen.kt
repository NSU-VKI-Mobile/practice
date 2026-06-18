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
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val application = LocalContext.current.applicationContext as DepositApplication

    val inputViewModel: InputViewModel = viewModel(
        factory = InputViewModelFactory(application.locator.calculateDepositUseCase)
    )

    Scaffold(
        topBar = {
            if (uiState.currentScreen == "main") {
                TopAppBar(title = { Text("Расчёт вкладов") })
            }
        },
        bottomBar = {
            if (uiState.currentScreen == "main") {
                NavigationBar {
                    NavigationBarItem(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Пользователи") }
                    )
                    NavigationBarItem(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text("Мои расчёты") }
                    )
                    NavigationBarItem(
                        selected = uiState.selectedTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Новый расчёт") }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.detailId != null -> {
                    HistoryDetailScreen(
                        calculationId = uiState.detailId!!,
                        onBack = { viewModel.hideDetail() }
                    )
                }
                uiState.resultData != null -> {
                    ResultScreen(
                        initialAmount = uiState.resultData!!["initialAmount"] ?: "",
                        periodMonths = uiState.resultData!!["periodMonths"] ?: "",
                        interestRate = uiState.resultData!!["interestRate"]?.toDoubleOrNull(),
                        monthlyTopUp = uiState.resultData!!["monthlyTopUp"],
                        onBack = { viewModel.hideResult() }
                    )
                }
                else -> {
                    when (uiState.selectedTab) {
                        0 -> UsersScreen()
                        1 -> HistoryScreen(
                            onNavigateToDetail = { id -> viewModel.showDetail(id) }
                        )
                        2 -> Step1Screen(
                            viewModel = inputViewModel,
                            onNavigateToHome = { viewModel.selectTab(0) },
                            onNext = { viewModel.navigateTo("step2") }
                        )
                    }
                }
            }
        }
    }

    if (uiState.currentScreen == "step2") {
        Step2Screen(
            viewModel = inputViewModel,
            onCalculate = { initialAmount, periodMonths, interestRate, monthlyTopUp ->
                viewModel.showResult(
                    mapOf(
                        "initialAmount" to initialAmount,
                        "periodMonths" to periodMonths,
                        "interestRate" to interestRate,
                        "monthlyTopUp" to monthlyTopUp
                    )
                )
                viewModel.navigateTo("result")
            },
            onBack = { viewModel.resetToMain() }
        )
    }
}