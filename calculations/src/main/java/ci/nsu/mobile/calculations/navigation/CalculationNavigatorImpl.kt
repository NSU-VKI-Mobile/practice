package ci.nsu.mobile.calculations.navigation

import androidx.navigation.NavHostController
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator
import ci.nsu.mobile.domain.navigation.Screens
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationsNavigatorImpl @Inject constructor() : CalculationsNavigator {

    override fun navigateToNewCalculation(navController: NavHostController, userId: Long) {
        navController.navigate(Screens.MainScreen.route) {
            launchSingleTop = true
        }
    }

    override fun navigateToMyCalculations(navController: NavHostController, userId: Long) {
        navController.navigate(Screens.HistoryScreen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    override fun openCalculationFlow(navController: NavHostController, userId: Long) {
        navigateToMyCalculations(navController, userId)
    }
}