package com.example.depositapp.ui.step2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.depositapp.domain.getRateForPeriod
import com.example.depositapp.ui.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    viewModel: DepositViewModel,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Определяем доступную ставку по сроку
    val months = uiState.periodMonthsText.toIntOrNull() ?: 0
    val availableRate = if (months > 0) getRateForPeriod(months) else 0.0

    // Если ставка не установлена — устанавливаем автоматически
    LaunchedEffect(availableRate) {
        if (availableRate > 0 && uiState.selectedRate != availableRate) {
            viewModel.onPeriodChanged(uiState.periodMonthsText) // триггер пересчёта ставки
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Шаг 2: Дополнительно") },
                navigationIcon = {
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

            // Карточка с информацией о ставке
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Процентная ставка",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    if (months <= 0) {
                        // Срок не указан — предупреждение
                        Text(
                            text = "Срок не указан. Вернитесь назад и введите срок.",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        // Показываем ставку и объясняем почему
                        Text(
                            text = "${availableRate}% годовых",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = when {
                                months < 6  -> "Срок менее 6 месяцев → ставка 15%"
                                months < 12 -> "Срок от 6 до 11 месяцев → ставка 10%"
                                else        -> "Срок от 12 месяцев → ставка 5%"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Поле ежемесячного пополнения (необязательное)
            OutlinedTextField(
                value = uiState.monthlyTopUpText,
                onValueChange = { viewModel.onMonthlyTopUpChanged(it) },
                label = { Text("Ежемесячное пополнение (₽)") },
                placeholder = { Text("Необязательно") },
                supportingText = { Text("Оставьте пустым если пополнений нет") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = onCalculate,
                    // Кнопка неактивна если срок не указан
                    enabled = months > 0,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Рассчитать")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
