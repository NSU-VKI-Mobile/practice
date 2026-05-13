package ci.nsu.mobile.main.navigation

import android.app.Activity
import android.content.Context
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator

class CalculationsNavigatorImpl : CalculationsNavigator {
    override fun navigateToNewCalculation(context: Context, userId: Long)  {}
    override fun navigateToMyCalculations(context: Context, userId: Long) {}
    override fun openCalculationFlow(activity: Activity, userId: Long)     {}
}