package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalParamsScreen(
    periodMonths: String,
    onBackClick: () -> Unit,
    onCalculateClick: (Double, String) -> Unit
) {
    var monthlyTopUp by remember { mutableStateOf("") }
    var selectedRate by remember { mutableStateOf<Double?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val period = periodMonths.toIntOrNull()
    val availableRates = when {
        period == null -> emptyList()
        period < 6 -> listOf(15.0)
        period < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    val canCalculate = selectedRate != null

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Дополнительные параметры",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Срок вклада: $periodMonths месяцев",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedRate?.let { "$it%" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate%") },
                        onClick = {
                            selectedRate = rate
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = monthlyTopUp,
            onValueChange = { newValue ->
                monthlyTopUp = newValue.replace("-", "").replace(",", ".")
            },
            label = { Text("Ежемесячное пополнение (руб)") },
            placeholder = { Text("необязательно") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (period == null) {
            Text(
                text = "Укажите корректный срок на предыдущем экране",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (canCalculate) {
                    onCalculateClick(selectedRate!!, monthlyTopUp)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canCalculate
        ) {
            Text("Рассчитать")
        }
    }
}