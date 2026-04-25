package ci.nsu.mobile.main.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.data.local.DepositEntity

@Composable
fun DepositView( deposit: DepositEntity?, modifier: Modifier = Modifier) {
    Column {
        Text("ID: ${deposit?.id}")
        Text("Начальный взнос: ${"%.1f".format(deposit?.initialAmount)}")
        Text("Срок: ${deposit?.months} мес.")
        Text("Ставка: ${deposit?.rate}%")
        Text("Пополнение: ${"%.1f".format(deposit?.monthlyTopUp)}")
        Text("Итог: ${"%.1f".format(deposit?.finalAmount)}")
        Text("Прибыль: ${"%.1f".format(deposit?.profit)}")
        Text("Дата: ${deposit?.date}")
    }
}