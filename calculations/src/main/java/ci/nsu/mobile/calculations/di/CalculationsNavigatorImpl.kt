package ci.nsu.mobile.calculations.di

import androidx.navigation.NavController
import ci.nsu.mobile.domain.navigation.CalculationsNavigator

class CalculationsNavigatorImpl(
    private val navController: NavController
) : CalculationsNavigator {
    override fun navigateToMyCalculations() {
        navController.navigate("my_calculations")
    }

    override fun navigateToNewCalculation() {
        navController.navigate("new_calculation")
    }
}