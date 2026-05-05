package ci.nsu.mobile.main.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.navigation.NavController
import ci.nsu.mobile.main.ScreenRoutes

class SecondStage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
            }
        }
    }
}

@Composable
fun SecondStageScreen(navController: NavController, viewModel: DepositViewModel, modifier: Modifier = Modifier)
{
    // Состояния для UI
    var expanded by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }

    val periodMonths = viewModel.periodMonths.toIntOrNull()

    val availableRates = remember(periodMonths) {
        when {
            periodMonths == null || periodMonths <= 0 -> emptyList()
            periodMonths < 6 -> listOf(15.0)
            periodMonths in 6..11 -> listOf(10.0)
            periodMonths >= 12 -> listOf(5.0)
            else -> emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выбор процентной ставки",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (periodMonths == null || periodMonths <= 0) {
                    showWarning = true
                    expanded = false
                } else {
                    showWarning = false
                    expanded = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = availableRates.isNotEmpty()
        ) {
            Text(
                if (viewModel.interestRate > 0) {
                    when (viewModel.interestRate) {
                        15.0 -> "15% (for a period of < 6 months)"
                        10.0 -> "10% (for a period of 6-12 months)"
                        5.0 -> "5% (for a period > 12 months)"
                        else -> "${viewModel.interestRate}%"
                    }
                } else {
                    "Select the interest rate"
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableRates.forEach { rate ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (rate) {
                                15.0 -> "15% (срок < 6 месяцев)"
                                10.0 -> "10% (срок 6-12 месяцев)"
                                5.0 -> "5% (срок ≥ 12 месяцев)"
                                else -> "$rate%"
                            }
                        )
                    },
                    onClick = {
                        viewModel.updateInterestRate(rate)
                        expanded = false
                        showWarning = false
                    }
                )
            }
        }

        if (showWarning) {
            Text(
                text = "The deposit period is not specified or is specified incorrectly"
            )
        }

        TextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.monthlyTopUp = it },
            label = { Text("Monthly replenishment (optional)") }
        )
        Button(
            onClick = {
                viewModel.monthlyTopUp = viewModel.monthlyTopUp.ifBlank { "0" }
                viewModel.calculateResult()
                navController.navigate(ScreenRoutes.Result.route)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Calculate/Go to the Result")
        }
        Button(
            onClick = {
                navController.navigate(ScreenRoutes.FirstStage.route)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back to the First Stage")
        }
    }
}