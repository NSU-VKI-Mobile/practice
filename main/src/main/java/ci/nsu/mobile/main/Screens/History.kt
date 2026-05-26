package ci.nsu.mobile.main.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.runtime.livedata.observeAsState

import ci.nsu.mobile.main.ViewModel.DepositViewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*

class History : ComponentActivity() {
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
fun HistoryStageScreen(navController: NavController, viewModel: DepositViewModel, modifier: Modifier = Modifier)
{
    val historyList = viewModel.history.observeAsState(emptyList()).value
    val dateFormat = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("Asia/Novosibirsk")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Calculation history",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (historyList.isEmpty()) {
            Text(
                text = "There are no saved calculations",
                modifier = Modifier.padding(16.dp)
            )
        }
        else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(historyList) { item ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { isExpanded = !isExpanded }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${item.userId}")
                            Text("Date: ${dateFormat.format(Date(item.calculationDate))}")
                            Text("Starting payment: ${item.initialAmount}")
                            Text("Result: ${String.format("%.2f", item.finalAmount)}")

                            if (isExpanded) {
                                Text("Period: ${item.periodMonths} мес.")
                                Text("Rate: ${item.interestRate}%")
                                Text("Replenishment: ${item.monthlyTopUp}/мес.")
                                Text("Accrued interest: ${String.format("%.2f", item.interestEarned)}")
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                navController.navigate("main")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to the Main Screen")
        }
    }
}