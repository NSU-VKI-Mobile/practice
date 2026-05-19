package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.domain.DepositResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: DepositResult?,
    isSaved: Boolean,
    saveMessage: String?,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Результат расчёта") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (result == null) {
                Text(
                    text = "Расчёт пока не выполнен.",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onHomeClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("В начало")
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InfoRow("Стартовый взнос", formatMoney(result.initialAmount))
                        InfoRow("Срок вклада", "${result.periodMonths} мес.")
                        InfoRow("Процентная ставка", formatPercent(result.interestRate))
                        InfoRow("Ежемесячное пополнение", formatTopUp(result.monthlyTopUp))
                        InfoRow("Итоговая сумма", formatMoney(result.finalAmount))
                        InfoRow("Начисленные проценты", formatMoney(result.interestEarned))
                    }
                }
                saveMessage?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSaveClick,
                        enabled = !isSaved,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isSaved) "Сохранено" else "Сохранить")
                    }
                    OutlinedButton(
                        onClick = onHomeClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("В начало")
                    }
                }
            }
        }
    }
}