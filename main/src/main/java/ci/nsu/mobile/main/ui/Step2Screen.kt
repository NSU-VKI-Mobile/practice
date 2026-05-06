package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Step2Screen(
    navController: NavController,
    amount: String,
    months: String
) {

    // очистка ввода
    fun clean(input: String): String = input.filter { it.isDigit() }

    val initialMonths = months.filter { it.isDigit() }.toIntOrNull() ?: 1

    // ===== логика =====
    fun monthsToRate(m: Int): Double = when {
        m <= 5 -> 15.0
        m <= 11 -> 10.0
        else -> 5.0
    }

    fun minMonths(rate: Double) = when (rate) {
        15.0 -> 1
        10.0 -> 6
        else -> 12
    }

    fun maxMonths(rate: Double) = when (rate) {
        15.0 -> 5
        10.0 -> 11
        else -> Int.MAX_VALUE
    }

    fun clamp(m: Int, rate: Double): Int {
        return m.coerceIn(minMonths(rate), maxMonths(rate))
    }

    fun rangeText(rate: Double): String = when (rate) {
        15.0 -> "1 - 5"
        10.0 -> "6 - 11"
        else -> "12+"
    }

    // ===== СОСТОЯНИЕ (ВАЖНО: правильно инициализируем) =====
    var monthsInput by remember { mutableStateOf(initialMonths.toString()) }
    var selectedRate by remember { mutableStateOf(monthsToRate(initialMonths)) }
    var topUp by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val rates = listOf(5.0, 10.0, 15.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Шаг 2", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        // ===== СТАВКА =====
        Box {
            Button(onClick = { expanded = true }) {
                Text("Ставка: $selectedRate%")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                rates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate %") },
                        onClick = {
                            selectedRate = rate
                            expanded = false

                            val m = monthsInput.toIntOrNull() ?: minMonths(rate)
                            monthsInput = clamp(m, rate).toString()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ===== МЕСЯЦЫ =====
        OutlinedTextField(
            value = monthsInput,
            onValueChange = {
                val cleaned = clean(it)
                monthsInput = cleaned

                val m = cleaned.toIntOrNull()
                if (m != null) {
                    val newRate = monthsToRate(m)
                    selectedRate = newRate
                    monthsInput = clamp(m, newRate).toString()
                }
            },
            label = { Text("Месяцы") }
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "Диапазон: ${rangeText(selectedRate)} месяцев",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(20.dp))

        // ===== ПОПОЛНЕНИЕ =====
        OutlinedTextField(
            value = topUp,
            onValueChange = {
                topUp = clean(it)
            },
            label = { Text("Пополнение") }
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                navController.navigate(
                    "result/$amount/$monthsInput/$selectedRate/$topUp"
                )
            }
        ) {
            Text("Рассчитать")
        }
    }
}