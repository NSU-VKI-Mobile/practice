package com.example.depositapp.ui.step1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.depositapp.ui.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    viewModel: DepositViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    // Подписываемся на UiState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Флаг — показывать ли ошибки (только после попытки перейти дальше)
    var showErrors by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Шаг 1: Параметры вклада") },
                navigationIcon = {
                    // Кнопка назад в шапке
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Поле стартового взноса
            val amountValid = uiState.initialAmountText.toDoubleOrNull()?.let { it > 0 } ?: false
            OutlinedTextField(
                value = uiState.initialAmountText,
                onValueChange = { viewModel.onInitialAmountChanged(it) },
                label = { Text("Стартовый взнос (₽)") },
                placeholder = { Text("Например: 100000") },
                // Показываем ошибку только если пытались перейти дальше
                isError = showErrors && !amountValid,
                supportingText = {
                    if (showErrors && !amountValid)
                        Text("Введите сумму больше 0", color = MaterialTheme.colorScheme.error)
                },
                singleLine = true,
                // keyboardType.Number — показываем числовую клавиатуру
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Поле срока
            val monthsValid = uiState.periodMonthsText.toIntOrNull()?.let { it > 0 } ?: false
            OutlinedTextField(
                value = uiState.periodMonthsText,
                onValueChange = { viewModel.onPeriodChanged(it) },
                label = { Text("Срок вклада (месяцев)") },
                placeholder = { Text("Например: 12") },
                isError = showErrors && !monthsValid,
                supportingText = {
                    if (showErrors && !monthsValid)
                        Text("Введите срок больше 0", color = MaterialTheme.colorScheme.error)
                    else if (monthsValid) {
                        // Подсказка какая ставка будет доступна
                        val months = uiState.periodMonthsText.toInt()
                        val hint = when {
                            months < 6  -> "Доступна ставка 15%"
                            months < 12 -> "Доступна ставка 10%"
                            else        -> "Доступна ставка 5%"
                        }
                        Text(hint, color = MaterialTheme.colorScheme.primary)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f)) // распихиваем кнопки вниз

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Кнопка "В начало"
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("В начало")
                }

                // Кнопка "Далее" — валидируем перед переходом
                Button(
                    onClick = {
                        showErrors = true  // включаем показ ошибок
                        if (viewModel.isStep1Valid()) onNext() // переходим только если всё ок
                    },
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Далее")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
