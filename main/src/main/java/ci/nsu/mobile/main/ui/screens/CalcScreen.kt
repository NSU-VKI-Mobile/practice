package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CalcScreen(
    onSaveClick: () -> Unit,
    onMainClick: () -> Unit
) {
    var spacer = "";
    Column() {
        Text("Стартовый взнос: ")
        Text("Срок вклада: ")
        Text("Процентная ставка: ")
        Text("Ежемесячное пополнение: ")
        Text("Итоговая сумма: ")
        Text("Начисленные проценты: ")

        Button(onClick = onSaveClick) {
            Text("Сохранить")
        }
        Button(onClick = onMainClick) {
            Text("В начало")
        }
    }
}