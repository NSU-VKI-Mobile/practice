package ci.nsu.mobile.domain.calculations

import android.app.Activity
import android.content.Context

interface CalculationsNavigator {
    fun navigateToNewCalculation(userId: Long)
    fun navigateToMyCalculations(userId: Long)
    fun openCalculationFlow(userId: Long)
}