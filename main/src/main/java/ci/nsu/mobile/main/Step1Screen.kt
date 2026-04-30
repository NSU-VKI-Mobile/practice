package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun Step1Screen(
    onBack: () -> Unit,
    onNext: (amount: String, months: String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Этап 1: Параметры вклада",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { input ->
                    amount = input

            },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = months,
            onValueChange = { input ->
                    months = input

            },
            label = { Text("Срок вклада (в месяцах)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }


            Button(
                onClick = { onNext(amount, months) },
                modifier = Modifier.weight(1f),
                enabled = amount.isNotBlank() && months.isNotBlank()
            ) {
                Text("Далее")
            }
        }
    }
}