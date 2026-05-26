package ci.nsu.mobile.domain.calculations

import android.app.Activity
import android.content.Context

interface CalculationsNavigator {
    fun navigateToNewCalculation(context: Context, userId: Long)
    fun navigateToMyCalculations(context: Context, userId: Long)
    fun openCalculationFlow(activity: Activity, userId: Long)
}