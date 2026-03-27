package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column() {
        Text("Расчёт вкладов")
        Button(onClick = onCalculateClick) {
            Text("Рассчитать")
        }
        Button(onClick = onHistoryClick) {
            Text("История расчётов")
        }
        Button(onClick = onCloseClick) {
            Text("Закрыть приложение")
        }
    }
}