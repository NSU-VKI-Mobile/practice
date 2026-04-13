package ci.nsu.mobile.main.presentation.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.data.DepositCalculationEntity
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModel
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreenContent(navToScreen: (String) -> Unit,
                         viewModel: DepositCalculationViewModel) {
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    val selectedState by viewModel.selectedState.collectAsStateWithLifecycle()

    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val openDialog = remember { mutableStateOf(false) }
    Scaffold() { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            items(historyState) { deposit ->
                ShortHistoryItemCard(
                    deposit = deposit,
                    dateFormat = dateFormat,
                    Click = {
                        viewModel.selectedDepositUpdate(deposit)
                        openDialog.value = true
                    }
                )
            }
        }
        if (openDialog.value && selectedState != null) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "INFO") },
                text = {
                    Column() {
                        Text(
                            "Стартовый взнос: ${selectedState!!.initialAmount}",
                            Modifier.padding(20.dp)
                        )
                        Spacer(Modifier.padding(5.dp))

                        Text(
                            "Срок вклада: ${selectedState!!.periodMonths}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Процентная ставка: ${selectedState!!.interestRate}%",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Ежемесячное пополнение: ${selectedState!!.monthlyTopUp}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Итоговая сумма: ${String.format("%.2f", selectedState!!.finalAmount)}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Начисленные проценты: ${String.format("%.2f",selectedState!!.interestEarned)}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Дата и время рассчета: ${dateFormat.format(Date(selectedState!!.calculationDate))}",
                            Modifier.padding(20.dp)
                        )
                    }
                },
                confirmButton = {
                    Button({ openDialog.value = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun ShortHistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat, Click: ()-> Unit) {
    Card(modifier = Modifier.padding(20.dp).clickable(onClick = Click)) {
        Text("DEPOSIT #${deposit.id}")
        Text("Итоговая сумма вклада:${deposit.finalAmount}")
        Text(dateFormat.format(Date(deposit.calculationDate)))
    }
}


