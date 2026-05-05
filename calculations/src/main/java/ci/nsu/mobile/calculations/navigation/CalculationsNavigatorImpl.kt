package ci.nsu.mobile.calculations.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import ci.nsu.mobile.calculations.CalculationsActivity
import ci.nsu.mobile.domain.calculations.CalculationsNavigator

class CalculationsNavigatorImpl : CalculationsNavigator {
    override fun navigateToNewCalculation(context: Context, userId: Long) {
        context.startActivity(calculationIntent(context, userId, CalculationsActivity.ROUTE_NEW_CALCULATION))
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        context.startActivity(calculationIntent(context, userId, CalculationsActivity.ROUTE_HISTORY))
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        activity.startActivity(calculationIntent(activity, userId, CalculationsActivity.ROUTE_NEW_CALCULATION))
    }

    private fun calculationIntent(context: Context, userId: Long, route: String): Intent {
        return Intent(context, CalculationsActivity::class.java)
            .putExtra(CalculationsActivity.EXTRA_USER_ID, userId)
            .putExtra(CalculationsActivity.EXTRA_START_ROUTE, route)
            .apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
    }
}
