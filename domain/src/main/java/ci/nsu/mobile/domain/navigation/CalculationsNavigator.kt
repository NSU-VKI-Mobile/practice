package ci.nsu.mobile.domain.navigation

import android.content.Context

interface CalculationsNavigator {
    fun navigateToNewCalculation(context: Context, userId: Long)
    fun navigateToMyCalculations(context: Context, userId: Long)
}