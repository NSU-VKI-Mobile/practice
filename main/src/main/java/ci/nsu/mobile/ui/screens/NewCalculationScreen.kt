package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen(
    vm: DepositViewModel,
    userLogin: String,
    onDone: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    var amount by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var monthlyTopUp by remember { mutableStateOf("") }

    val rates = listOf("5", "10", "15")
    var expanded by remember { mutableStateOf(false) }
    var selectedRate by remember { mutableStateOf(rates[0]) }

    var showWarningDialog by remember { mutableStateOf(false) }

    val periodInt = period.toIntOrNull() ?: 0

    val recommendation = when {
        periodInt < 6 -> "Рекомендуемый процент: 15% для срока < 6 мес."
        periodInt in 6..11 -> "Рекомендуемый процент: 10% для срока 6-11 мес."
        else -> "Рекомендуемый процент: 5% для срока >= 12 мес."
    }

    val isRecommended = when {
        periodInt < 6 && selectedRate == "15" -> true
        periodInt in 6..11 && selectedRate == "10" -> true
        periodInt >= 12 && selectedRate == "5" -> true
        else -> false
    }

    val onCalculate = {
        val a = amount.toLongOrNull() ?: 0L
        val p = period.toIntOrNull() ?: 0
        val r = selectedRate.toIntOrNull() ?: 0
        val t = monthlyTopUp.toLongOrNull() ?: 0L

        var result = a.toDouble()

        repeat(p) {
            result += result * r / 100.0 / 12.0 + t
        }

        val finalAmount = result.toLong()
        val interestEarned = (result - a - (t * p)).toLong()

        val deposit = DepositCalculation(
            userLogin = userLogin,
            initialAmount = a,
            periodMonths = p,
            interestRate = r,
            monthlyTopUp = t,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        vm.add(deposit, userLogin)
        onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (step == 1)
                "Шаг 1: Основные данные"
            else
                "Шаг 2: Параметры вклада",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        if (step == 1) {

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it.filter { ch -> ch.isDigit() }
                },
                label = { Text("Стартовый взнос") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = period,
                onValueChange = {
                    period = it.filter { ch -> ch.isDigit() }
                },
                label = { Text("Срок вклада (мес.)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (amount.isNotEmpty() && period.isNotEmpty()) {
                        step = 2
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text("Далее")
            }

        } else {

            Text(
                text = "Выберите процентную ставку:",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodyLarge
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = "$selectedRate %",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    rates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate %") },
                            onClick = {
                                selectedRate = rate
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = monthlyTopUp,
                onValueChange = {
                    monthlyTopUp = it.filter { ch -> ch.isDigit() }
                },
                label = { Text("Ежемесячное пополнение (опц.)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isRecommended) {
                        onCalculate()
                    } else {
                        showWarningDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text("Рассчитать")
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = { step = 1 }
            ) {
                Text("Назад")
            }
        }
    }

    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = {
                showWarningDialog = false
            },
            title = {
                Text("Выбор процента")
            },
            text = {
                Text(
                    "Вы выбрали процент $selectedRate%. " +
                            "$recommendation\nПродолжить?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showWarningDialog = false
                        onCalculate()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showWarningDialog = false
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}