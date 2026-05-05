package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputFirstScreen(
    onNavigateBack: () -> Unit,  // возврат на главный
    onNavigateToSecond: (Double, Int) -> Unit // переход на второй экран с данными
) {
    var initialAmount by remember { mutableStateOf("") } // строка из поля ввода
    var periodMonths by remember { mutableStateOf("") } // строка из поля ввода
    var errorMessage by remember { mutableStateOf("") } // сообщение об ошибке

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Основные параметры вклада",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = initialAmount,
            onValueChange = { initialAmount = it },
            label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = periodMonths,
            onValueChange = { periodMonths = it },
            label = { Text("Срок вклада (месяцы)") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }

            Button(
                onClick = {
                    val amount = initialAmount.toDoubleOrNull()
                    val period = periodMonths.toIntOrNull()

                    if (amount == null || amount <= 0.0) {
                        errorMessage = "Введите корректную сумму (>0)"
                    } else if (period == null || period <= 0) {
                        errorMessage = "Введите корректный срок (>0)"
                    } else { //только если нет ошибок
                        errorMessage = ""
                        onNavigateToSecond(amount, period)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Далее")
            }
        }
    }
}