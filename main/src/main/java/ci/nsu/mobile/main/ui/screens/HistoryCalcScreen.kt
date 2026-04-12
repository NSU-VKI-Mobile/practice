package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.db.Deposit
import ci.nsu.mobile.main.vm.DepositsViewModel

@Composable
fun HistoryCalcScreen(
    onBackClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel()
) {
    val depositList by viewModel.allDepositList.collectAsStateWithLifecycle()
    var selectedDeposit by remember { mutableStateOf<Deposit?>(null) }
    Box() {
        LazyColumn(
            modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)) {
            items(depositList){item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedDeposit = item
                            }
                            .padding(16.dp)
                    ) {
                        Text("Дата: " + viewModel.formatTime(item.calculationDate))
                        Text("Стартовый взнос: " + item.initialAmount.toString())
                        Text("Итоговая сумма: " + item.finalAmount.toString())
                    }
                }
            }
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Назад")
        }
    }
    selectedDeposit?.let { obj ->
        SingleShowDepositDialogScreen(
            onDismiss = { selectedDeposit = null },
            thisDeposit = obj,
            viewModel = viewModel
        )
    }
}

