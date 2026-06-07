package ci.nsu.mobile.domain.interfaces

import androidx.navigation.NavHostController

interface CalculationsNavigator {
    fun navigateToNewCalculation(navController: NavHostController, userId: Long)
    fun navigateToMyCalculations(navController: NavHostController, userId: Long)
    fun openCalculationFlow(navController: NavHostController, userId: Long)
}