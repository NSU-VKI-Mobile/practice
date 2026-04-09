package com.example.depositcalculator

import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.depositcalculator.screen.*
import com.example.depositcalculator.viewmodel.DepositeViewModel
import com.example.depositcalculator.DepositCalculator
import com.example.depositcalculator.DepositEntity

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val viewModel: DepositeViewModel = viewModel()

    var tempAmount by remember { mutableStateOf(0.0) }
    var tempMonths by remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToStep1 = { navController.navigate("step1")},
                onNavigateToHidtory = {navController.navigate("history")},
                onCloseApp = { /* finish в MainActivity */}
            )

        }
        composable("step1") {
            Step1Screen(
                onNext = { amount, months ->
                    tempAmount = amount
                    tempMonths = months
                    navController.navigate("step2")
                },
                onBack = { navController.popBackStack()}
            )
        }

        composable("step2") {
            Step2Screen(
                months = tempMonths,
                onBack = {navController.popBackStack()},
                onCalculate = { rate, topUp ->
                    val deposit = DepositCalculator.calculate(tempAmount, tempMonths, rate, topUp)
                    viewModel.saveDeposit((DepositEntity.fromDomain(deposit)))
                    navController.navigate("result") {
                        popUpTo("step1") { inclusive = true}
                    }
                }
            )
        }

        composable("result") {
            val lastDeposit = viewModel.history.value.lastOrNull()
            if (lastDeposit != null) {
                ResultScreen(
                    deposit = DepositCalculator.calculate(
                        lastDeposit.initialAmount,
                        lastDeposit.periodMonths,
                        lastDeposit.interestRate,
                        lastDeposit.monthlyTopUp
                    ),
                    onBackToMain = { navController.navigate("main") { popUpTo("main") {inclusive = true} } },
                    onSave = {}
                )
            }
        }

        composable("history") {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}