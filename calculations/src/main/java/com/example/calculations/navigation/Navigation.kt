package com.example.calculations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calculations.ui.screens.FirstScreenContent
import com.example.calculations.ui.screens.MainScreenContent
import com.example.calculations.ui.screens.ResultScreenContent
import com.example.calculations.ui.screens.SecondScreenContent
import com.example.calculations.viewmodel.DepositCalculationViewModel

sealed class Screens(val route: String) {
    object MainScreen: Screens("MainScreen")
    object FirstScreen: Screens("FirstScreen")
    object SecondScreen: Screens("SecondScreen")
    object ResultScreen: Screens("ResultScreen")
}

@Composable
fun Navigation(
    navController: NavHostController,
    depositCalculationViewModel: DepositCalculationViewModel,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route,
        modifier = modifier
    ) {
        composable(Screens.MainScreen.route) {
            MainScreenContent(
                { navigateTo -> navController.navigate(navigateTo) },
                depositCalculationViewModel
            )
        }
        composable(Screens.FirstScreen.route) {
            FirstScreenContent(
                { navigateTo -> navController.navigate(navigateTo) },
                depositCalculationViewModel
            )
        }
        composable(Screens.SecondScreen.route) {
            SecondScreenContent(
                { navigateTo -> navController.navigate(navigateTo) },
                depositCalculationViewModel
            )
        }
        composable(Screens.ResultScreen.route) {
            ResultScreenContent(
                { navigateTo -> navController.navigate(navigateTo) },
                depositCalculationViewModel
            )
        }
    }
}