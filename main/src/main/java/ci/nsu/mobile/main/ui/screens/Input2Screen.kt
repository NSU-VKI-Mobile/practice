package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Input2Screen(
    onBackClick: () -> Unit,
    onCalcClick: () -> Unit
) {
    var spacer = "";
    Column() {
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer = newText },
            label = { Text("Введите текст") }
        )
        OutlinedTextField(
            value = spacer,
            onValueChange = { newText -> spacer = newText },
            label = { Text("Введите сумму ежемесячного пополнения (необязательное)") }
        )

        Button(onClick = onBackClick) {
            Text("Назад")
        }
        Button(onClick = onCalcClick) {
            Text("Рассчитать")
        }
    }
}