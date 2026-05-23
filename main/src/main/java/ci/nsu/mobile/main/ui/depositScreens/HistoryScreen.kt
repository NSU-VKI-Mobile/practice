package ci.nsu.mobile.main.ui.depositScreens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import ci.nsu.mobile.main.navigation.Screen
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreenContent(navToScreen: (String) -> Unit,
                         viewModel: HistoryDepositsViewModel) {
    val historyState by viewModel.state.collectAsStateWithLifecycle()
    val selectedState = historyState.selectedDeposit

    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val openDialog = remember { mutableStateOf(false) }
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn(
                modifier = Modifier.padding(10.dp).weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(historyState.deposits) { deposit ->
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

            Button(onClick = { navToScreen(Screen.MainScreen.route) },
                modifier = Modifier.padding(10.dp).width(150.dp)) {
                Text("Назад")
            }
        }
        if (openDialog.value && selectedState != null) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "INFO about deposit ${selectedState!!.id}") },
                text = {
                    Column() {
                        Text(
                            "Стартовый взнос: ${selectedState!!.initialAmount}₽",
                            Modifier.padding(20.dp)
                        )
                        Text(
                            "Срок вклада (в месяцах): ${selectedState!!.periodMonths}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Процентная ставка: ${selectedState!!.interestRate}%",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        val mot = selectedState!!.monthlyTopUp
                        if (mot == null) {
                            Text("Ежемесячное пополнение: 0₽", Modifier.padding(20.dp,  0.dp))
                        }
                        else {
                            Text("Ежемесячное пополнение: ${mot}₽", Modifier.padding(20.dp,  0.dp))
                        }
                        Text(
                            "Итоговая сумма: ${String.format("%.2f", selectedState!!.finalAmount)}₽",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Начисленные проценты: ${String.format("%.2f",selectedState!!.interestEarned)}₽",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Дата и время рассчета: ${dateFormat.format(Date(selectedState!!.calculationDate))}",
                            Modifier.padding(20.dp)
                        )
                    }
                },
                confirmButton = {
                    Row(modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        Button({ openDialog.value = false }, modifier = Modifier.width(150.dp)) {
                            Text("OK")
                        }
                    }
                }
            )
        }
    }

}

@Composable
fun ShortHistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat, Click: ()-> Unit) {
    Card(modifier = Modifier.padding(20.dp).clickable(onClick = Click).width(400.dp)) {
        Text("DEPOSIT #${deposit.id}",
            modifier = Modifier.padding(10.dp))
        Text("Итоговая сумма вклада:${String.format("%.2f", deposit.finalAmount)}₽",
            modifier = Modifier.padding(horizontal = 10.dp))
        Text("Процентная ставка:${deposit.interestRate}%",
            modifier = Modifier.padding(horizontal = 10.dp))
        Text(dateFormat.format(Date(deposit.calculationDate)),
            modifier = Modifier.padding(10.dp))
    }
}


