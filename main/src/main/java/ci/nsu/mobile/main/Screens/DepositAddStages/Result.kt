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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxWidth
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import androidx.navigation.NavController

class Result : ComponentActivity() {
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
fun ResultStageScreen(navController: NavController, viewModel: DepositViewModel, modifier: Modifier = Modifier)
{
    var showSaveSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Calculation result",
            fontSize = 20.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Starting payment: ${viewModel.initialAmount}")
                Text("Period: ${viewModel.periodMonths} мес.")
                Text("Rate: ${viewModel.interestRate}%")
                Text("Replenishment: ${viewModel.monthlyTopUp.ifBlank { "0" }}/мес.")
                Text("Accrued interest: ${String.format("%.2f", viewModel.interestEarned)}")
                Text("Total amount: ${String.format("%.2f", viewModel.finalAmount)}")
            }
        }
        if (showSaveSuccess) {
            Text(
                "The calculation is saved",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Button(
            onClick = {
                viewModel.saveCalculation()
                //viewModel.clearData()
                showSaveSuccess = true
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Deposit")
        }
    }
}