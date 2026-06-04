package ci.nsu.mobile.calculations

import android.app.Activity
import android.content.Context
import ci.nsu.mobile.domain.CalculationsNavigator

/**
 * Реализация навигации для модуля calculations.
 * Принимает действия (лямбды) из основного приложения.
 */
class CalculationsNavigatorImpl(
    private val onNavigateToNewCalculation: (userId: Long) -> Unit,
    private val onNavigateToMyCalculations: (userId: Long) -> Unit
) : CalculationsNavigator {

    override fun navigateToNewCalculation(context: Context, userId: Long) {
        onNavigateToNewCalculation(userId)
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        onNavigateToMyCalculations(userId)
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        onNavigateToNewCalculation(userId)
    }
}