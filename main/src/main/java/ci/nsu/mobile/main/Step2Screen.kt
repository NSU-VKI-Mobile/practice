package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    months: Int,
    onBack: () -> Unit,
    onCalculate: (rate: Double, topUp: Double) -> Unit
) {
    var topUp by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val availableRate = when {
        months < 6 -> 15.0
        months in 6..11 -> 10.0
        months >= 12 -> 5.0
        else -> 0.0
    }

    var selectedRate by remember { mutableStateOf(availableRate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Этап 2: Дополнительные параметры",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = "$selectedRate%",
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("$availableRate% (Доступно для вашего срока)") },
                    onClick = {
                        selectedRate = availableRate
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = topUp,
            onValueChange =  { input ->
                    topUp = input
            },
            label = { Text("Ежемесячное пополнение (₽)") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0.0") },
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
                Text("Назад")
            }

            Button(
                onClick = {
                    onCalculate(selectedRate, topUp.toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}