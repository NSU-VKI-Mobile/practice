package ci.nsu.mobile.calculations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ci.nsu.mobile.calculations.ui.DepositCalculatorScreen
import ci.nsu.mobile.calculations.ui.HistoryScreen

class CalculationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRoute = intent.getStringExtra(EXTRA_START_ROUTE) ?: ROUTE_NEW_CALCULATION
        val userId = intent.getLongExtra(EXTRA_USER_ID, 0L)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculationFlow(startRoute = startRoute, userId = userId)
                }
            }
        }
    }

    companion object {
        const val EXTRA_START_ROUTE = "ci.nsu.mobile.calculations.START_ROUTE"
        const val EXTRA_USER_ID = "ci.nsu.mobile.calculations.USER_ID"
        const val ROUTE_NEW_CALCULATION = "new_calculation"
        const val ROUTE_HISTORY = "history"
    }
}

@Composable
fun CalculationFlow(startRoute: String, userId: Long) {
    when (startRoute) {
        CalculationsActivity.ROUTE_HISTORY -> HistoryScreen(userId = userId)
        else -> DepositCalculatorScreen(userId = userId)
    }
}
