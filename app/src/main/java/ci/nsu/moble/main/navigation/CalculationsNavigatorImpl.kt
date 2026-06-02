package ci.nsu.moble.main.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import ci.nsu.moble.domain.interfaces.CalculationsNavigator

class CalculationsNavigatorImpl(
    private val navController: NavController
) : CalculationsNavigator {

    override fun navigateToNewCalculation(context: Context, userId: Long) {
        navController.navigate("new_deposit")
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        navController.navigate("history")
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        navController.navigate("new_deposit")
    }

    override fun navigateToHistory(context: Context) {
        navController.navigate("history")
    }
}