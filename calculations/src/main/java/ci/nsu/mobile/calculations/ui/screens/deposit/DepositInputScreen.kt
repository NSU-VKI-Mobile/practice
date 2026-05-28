package ci.nsu.mobile.calculations.ui.screens.deposit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DepositInputScreen(
    onBackClick: () -> Unit,
    onNextClick: (String, String) -> Unit
) {
    var initialAmount = remember { mutableStateOf("") }
    var periodMonths = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Экран 1: Ввод параметров",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 50.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = initialAmount.value,
            onValueChange = { initialAmount.value = it },
            label = { Text("Стартовый взнос (руб)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = periodMonths.value,
            onValueChange = { periodMonths.value = it },
            label = { Text("Срок вклада (месяцы)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        val isValid = initialAmount.value.isNotEmpty() &&
                periodMonths.value.isNotEmpty() &&
                initialAmount.value.toDoubleOrNull()?.let { it > 0 } == true &&
                periodMonths.value.toIntOrNull()?.let { it > 0 } == true

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("В начало")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isValid) {
                    onNextClick(initialAmount.value, periodMonths.value)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = isValid
        ) {
            Text("Далее")
        }
    }
}