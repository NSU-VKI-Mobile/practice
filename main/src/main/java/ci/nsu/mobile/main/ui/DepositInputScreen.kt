package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DepositInputScreen(
    amount: String,
    onAmountChange: (String) -> Unit,
    period: String,
    onPeriodChange: (String) -> Unit,
    onNextClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val canProceed = amount.isNotBlank() && period.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Этап 1: Основные параметры", fontSize = 20.sp, modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                onAmountChange(newValue.replace("-", "").replace(",", "."))
            },
            label = { Text("Стартовый взнос (руб)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Вызываем цифровую клавиатуру
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = period,
            onValueChange = { newValue ->
                onPeriodChange(newValue.replace("-", "").replace(",", "."))
            },
            label = { Text("Срок вклада (мес)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = onNextClick, enabled = canProceed, modifier = Modifier.fillMaxWidth()) { Text("Далее") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onHomeClick, modifier = Modifier.fillMaxWidth()) { Text("В начало") }
    }
}