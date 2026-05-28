package ci.nsu.mobile.calculations.ui.screens.deposit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    initialAmount: String,
    periodMonths: String,
    interestRate: Double?,
    monthlyTopUp: String,
    finalAmount: Double,
    interestEarned: Double,
    onSaveClick: () -> Unit,
    onBackToHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Результат расчёта",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 50.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Параметры вклада",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Стартовый взнос: $initialAmount руб.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Срок вклада: $periodMonths месяцев",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Процентная ставка: ${interestRate?.let { "$it%" } ?: "не выбрана"}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                if (monthlyTopUp.isNotEmpty() && monthlyTopUp.toDoubleOrNull() != null && monthlyTopUp.toDoubleOrNull()!! > 0.0) {
                    Text(
                        text = "Ежемесячное пополнение: $monthlyTopUp руб.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Итоговая сумма: $finalAmount руб.",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Начисленные проценты: $interestEarned руб.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToHomeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("В начало")
        }
    }
}