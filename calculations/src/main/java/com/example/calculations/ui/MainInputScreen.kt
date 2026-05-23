package com.example.calculations.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculations.vm.DepositsViewModel

sealed class Screen(val route: String) {
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
}

@Composable
fun MainInputScreen(
    depositsViewModel: DepositsViewModel,
    userId: Long
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Input1.route
    ) {
        composable(Screen.Input1.route) {
            Input1Screen(
                onNextClick = { navController.navigate(Screen.Input2.route) },
                viewModel = depositsViewModel
            )
        }
        composable(Screen.Input2.route) {
            Input2Screen(
                onBackClick = { navController.popBackStack() },
                onCalcClick = { navController.navigate(Screen.Calc.route) },
                viewModel = depositsViewModel
            )
        }
        composable(Screen.Calc.route) {
            CalcScreen(
                onMainClick = {
                    navController.popBackStack(
                        Screen.Input1.route,
                        inclusive = false
                    )
                },
                viewModel = depositsViewModel,
                userId
            )
        }
    }
}
