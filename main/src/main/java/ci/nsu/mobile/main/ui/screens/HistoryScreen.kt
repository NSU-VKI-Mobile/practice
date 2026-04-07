package ci.nsu.mobile.main.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreenContent(navScreens: NavController,
                         viewModel: DepositCalculationViewModel) {
    var isRed by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    Scaffold() { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            items(historyState) { deposit ->
                HistoryItemCard(
                    deposit = deposit,
                    dateFormat = dateFormat,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun HistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(20.dp)) {
        Text("DEPOSIT #${deposit.id}")
        Text("Итоговая сумма вклада:${deposit.finalAmount}\nСрок ${deposit.periodMonths}\nПроцент:${deposit.interestRate}")
        Text(dateFormat.format(Date(deposit.calculationDate)))
    }
}
