package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Input1Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var spacer = "";
    Column() {
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer = newText },
            label = { Text("Введите стартовый взнос") }
        )
        OutlinedTextField(
            value = spacer,
            onValueChange = { newText -> spacer = newText },
            label = { Text("Введите срок вклада в месяцах") }
        )

        Button(onClick = onBackClick) {
            Text("В начало")
        }
        Button(onClick = onNextClick) {
            Text("Далее")
        }
    }
}