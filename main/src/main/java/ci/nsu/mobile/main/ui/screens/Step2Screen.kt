package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    startAmount: Double,
    months: Int,
    onBackClick: () -> Unit,
    onCalculateClick: (Double, Double) -> Unit
) {
    val availableRate = when {
        months < 6 -> 15.0
        months < 12 -> 10.0
        else -> 5.0
    }
    val rateLabel = "$availableRate%"

    var expanded by remember { mutableStateOf(false) }
    var monthlyAdditionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Text(
            text = "Шаг 2 из 2: Дополнительные параметры",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Процентная ставка:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = rateLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ставка") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(rateLabel) },
                    onClick = { expanded = false }
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Text(
                text = when {
                    months < 6 -> "Для срока менее 6 месяцев ставка: 15%"
                    months < 12 -> "Для срока от 6 до 11 месяцев ставка: 10%"
                    else -> "Для срока от 12 месяцев ставка: 5%"
                },
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = Color(0xFF1565C0)
            )
        }

        OutlinedTextField(
            value = monthlyAdditionText,
            onValueChange = { monthlyAdditionText = it },
            label = { Text("Ежемесячное пополнение (руб.)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    val monthlyAddition = monthlyAdditionText.toDoubleOrNull() ?: 0.0
                    onCalculateClick(availableRate, monthlyAddition)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Text("Рассчитать")
            }
        }
    }
}