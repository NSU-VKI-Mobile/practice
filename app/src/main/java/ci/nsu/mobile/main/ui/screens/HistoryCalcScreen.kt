package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.entity.Deposit
import ci.nsu.mobile.main.vm.DepositsViewModel

@Composable
fun HistoryCalcScreen(
    viewModel: DepositsViewModel = viewModel()
) {
    val depositList by viewModel.userDeposits.collectAsStateWithLifecycle()
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
                        Text(stringResource(R.string.date) + ": " + viewModel.formatTime(item.calculationDate))
                        Text(stringResource(R.string.initialAmount) + ": " + item.initialAmount.toString())
                        Text(stringResource(R.string.finalAmount) + ": " + item.finalAmount.toString())
                    }
                }
            }
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

