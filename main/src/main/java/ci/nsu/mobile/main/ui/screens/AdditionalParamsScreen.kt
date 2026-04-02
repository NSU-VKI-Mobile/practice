package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
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
fun AdditionalParamsScreen(
    periodMonths: String,
    onBackClick: () -> Unit,
    onCalculateClick: (Double, String) -> Unit
) {
    var monthlyTopUp = remember { mutableStateOf("") }
    var selectedRate = remember { mutableStateOf<Double?>(null) }
    var expanded = remember { mutableStateOf(false) }

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
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Выбор ставки: [будет позже]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Ежемесячное пополнение: [будет позже]",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
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
            onClick = onCalculateClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Рассчитать")
        }
    }
}