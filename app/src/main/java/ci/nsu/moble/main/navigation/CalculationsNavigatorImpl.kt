package ci.nsu.moble.main.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import ci.nsu.moble.domain.interfaces.CalculationsNavigator
import ci.nsu.moble.main.MainActivity

class CalculationsNavigatorImpl : CalculationsNavigator {
    override fun navigateToNewCalculation(context: Context, userId: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "new_deposit")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "history")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("destination", "new_deposit")
            putExtra("userId", userId)
        }
        activity.startActivityForResult(intent, 100)
    }

    override fun navigateToHistory(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "history")
        }
        context.startActivity(intent)
    }
}