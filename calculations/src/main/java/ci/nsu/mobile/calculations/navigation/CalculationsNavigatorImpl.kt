package ci.nsu.mobile.calculations.navigation

import androidx.navigation.NavController
import ci.nsu.mobile.domain.calculations.CalculationsNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationsNavigatorImpl @Inject constructor() : CalculationsNavigator {

    private var navController: NavController? = null

    fun setNavController(controller: NavController) {
        navController = controller
    }

    override fun navigateToNewCalculation(userId: Long) {
        navController?.navigate("new_calculation")
    }

    override fun navigateToMyCalculations(userId: Long) {
        navController?.navigate("history")
    }

    override fun openCalculationFlow(userId: Long) {
        navController?.navigate("new_calculation")
    }
}