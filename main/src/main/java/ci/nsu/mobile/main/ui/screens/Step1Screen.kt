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

@Composable
fun Step1Screen(
    onBackClick: () -> Unit,
    onNextClick: (Double, Int) -> Unit
) {
    var startAmountText by remember { mutableStateOf("") }
    var monthsText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            text = "Шаг 1 из 2: Основные параметры",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = startAmountText,
            onValueChange = {
                startAmountText = it
                errorMessage = ""
            },
            label = { Text("Стартовый взнос (руб.) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = monthsText,
            onValueChange = {
                monthsText = it
                errorMessage = ""
            },
            label = { Text("Срок вклада (месяцев) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

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
                Text("В начало")
            }

            Button(
                onClick = {
                    when {
                        startAmountText.isEmpty() ->
                            errorMessage = "Введите стартовый взнос!"
                        monthsText.isEmpty() ->
                            errorMessage = "Введите срок вклада!"
                        else -> {
                            val start = startAmountText.toDoubleOrNull()
                            val months = monthsText.toIntOrNull()
                            when {
                                start == null || start <= 0 ->
                                    errorMessage = "Введите корректный стартовый взнос!"
                                months == null || months <= 0 ->
                                    errorMessage = "Введите корректный срок вклада!"
                                else -> onNextClick(start, months)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Text("Далее")
            }
        }
    }
}