package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HistoryCalcScreen(
    onBackClick: () -> Unit,
) {
    var spacer = "";
    Column() {
        Text("История: ")
        Button(onClick = onBackClick) {
            Text("Назад")
        }
    }
}