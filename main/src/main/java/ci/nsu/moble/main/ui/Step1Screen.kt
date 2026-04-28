package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositViewModel
import ci.nsu.moble.main.Navigation

@Composable
fun Step1Screen(nav: NavController, vm: DepositViewModel) {
    var amountError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Шаг 1: Основные параметры", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = vm.initialAmount,
            onValueChange = { vm.initialAmount = it; amountError = false },
            label = { Text("Стартовый взнос (₽)") },
            isError = amountError,
            supportingText = { if (amountError) Text("Введите корректную сумму") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        Text("Процентная ставка", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))

        listOf(15.0, 10.0, 5.0).forEach { rate ->
            RateCard(
                rate = rate,
                description = when (rate) {
                    15.0 -> "Срок до 6 месяцев"
                    10.0 -> "Срок от 6 до 11 месяцев"
                    else -> "Срок от 12 месяцев"
                },
                isSelected = vm.selectedRate == rate,
                onClick = { vm.selectedRate = rate }
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { nav.navigate(Navigation.MAIN) { popUpTo(Navigation.MAIN) { inclusive = true } } },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
            Button(
                onClick = {
                    val amount = vm.initialAmount.toDoubleOrNull()
                    amountError = amount == null || amount <= 0
                    if (!amountError) nav.navigate(Navigation.STEP2)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Далее")
            }
        }
    }
}

// Карточка выбора ставки
@Composable
private fun RateCard(
    rate: Double,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer  // выделенная
            else
                MaterialTheme.colorScheme.surfaceVariant    // обычная
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$rate%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Галочка если выбрано
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}