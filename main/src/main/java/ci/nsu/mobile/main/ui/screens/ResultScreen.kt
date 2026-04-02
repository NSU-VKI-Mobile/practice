package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
            text = "Экран результата",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 50.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Стартовый взнос: [здесь будут данные]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Срок вклада: [здесь будут данные]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Процентная ставка: [здесь будут данные]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Итоговая сумма: [здесь будут данные]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

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