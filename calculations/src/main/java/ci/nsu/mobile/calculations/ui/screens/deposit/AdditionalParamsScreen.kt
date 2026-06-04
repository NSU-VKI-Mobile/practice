package ci.nsu.mobile.calculations.ui.screens.deposit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalParamsScreen(
    periodMonths: String,
    onBackClick: () -> Unit,
    onCalculateClick: (Double, String) -> Unit
) {
    var monthlyTopUp = remember { mutableStateOf("") }
    var selectedRate = remember { mutableStateOf<Double?>(null) }
    var expanded = remember { mutableStateOf(false) }

    val period = periodMonths.toIntOrNull()
    val availableRates = when {
        period == null -> emptyList()
        period < 6 -> listOf(15.0)
        period < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    val canCalculate = selectedRate.value != null

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Экран 2: Дополнительные параметры",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 50.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Срок вклада: $periodMonths месяцев",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it }
        ) {
            OutlinedTextField(
                value = selectedRate.value?.let { "$it%" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите процентную ставку") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate%") },
                        onClick = {
                            selectedRate.value = rate
                            expanded.value = false
                        }
                    )
                }
            }
        }

        if (period == null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Укажите корректный срок на предыдущем экране",
                color = Color.Red,
                fontSize = 14.sp
            )
        } else if (availableRates.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Нет доступных ставок для указанного срока",
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = monthlyTopUp.value,
            onValueChange = { monthlyTopUp.value = it },
            label = { Text("Ежемесячное пополнение (руб) (необязательно)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (canCalculate) {
                    onCalculateClick(selectedRate.value!!, monthlyTopUp.value)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = canCalculate
        ) {
            Text("Рассчитать")
        }
    }
}