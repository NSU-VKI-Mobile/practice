package ci.nsu.mobile.main.presentation.ui.screens

import android.R.attr.onClick
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.data.DepositCalculationEntity
import ci.nsu.mobile.main.presentation.ui.theme.backgroundLight
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModel
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreenContent(navToScreen: (String) -> Unit,
                         viewModel: DepositCalculationViewModel) {
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    Scaffold() { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            items(historyState) { deposit ->
                ShortHistoryItemCard(
                    deposit = deposit,
                    dateFormat = dateFormat,
                )
            }
        }
    }
}

@Composable
fun ShortHistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat) {
    Card(modifier = Modifier.padding(20.dp)) {
        Text("DEPOSIT #${deposit.id}", modifier = Modifier.clickable(
            /* todo типа окна для вывода подробной инфы с крестиком */
        ))
        Text("Итоговая сумма вклада:${deposit.finalAmount}")
        Text(dateFormat.format(Date(deposit.calculationDate)))
    }
}

@Composable
fun LongHistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat) {
    Card(modifier = Modifier.padding(20.dp)) {
        Text("DEPOSIT #${deposit.id}")
        Text("Стартовая сумма вклада:${deposit.initialAmount}")
        Text("Итоговая сумма вклада:${deposit.finalAmount}")
        Text(dateFormat.format(Date(deposit.calculationDate)))
    }
}


