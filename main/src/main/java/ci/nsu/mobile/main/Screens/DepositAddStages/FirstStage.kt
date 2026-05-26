package ci.nsu.mobile.main.Screens.DepositAddStages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.material3.TextField

import ci.nsu.mobile.main.ViewModel.DepositViewModel
import android.widget.Toast
import androidx.navigation.NavController

class FirstStage : ComponentActivity() {
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
fun FirstStageScreen(navController: NavController, viewModel: DepositViewModel, modifier: Modifier = Modifier)
{
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextField(
            value = viewModel.initialAmount,
            label = { Text("Starting payment") },
            onValueChange = { viewModel.initialAmount = it },
            isError = viewModel.initialAmount.isEmpty() &&
                    (viewModel.initialAmount.toDoubleOrNull() ?: 0.0) <= 0,
        )
        TextField(
            value = viewModel.periodMonths,
            onValueChange = { viewModel.periodMonths = it },
            label = { Text("Deposit period (months)") },
            isError = viewModel.periodMonths.isNotBlank() &&
                    (viewModel.periodMonths.toIntOrNull() ?: 0) <= 0,
        )

        Button(
            onClick = {
                if (viewModel.initialAmount.isBlank()
                    || viewModel.periodMonths.isBlank()
                    || viewModel.initialAmount.toInt() <= 0  ) {
                    Toast.makeText(context, "Fill in all required fields", Toast.LENGTH_SHORT).show()

                } else {
                    viewModel.interestRate = viewModel.determineInterestRate()
                    navController.navigate("second_stage")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Further")
        }
    }
}