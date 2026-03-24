package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme


@Composable
fun SecondScreenContent() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var expanded by remember { mutableStateOf(true) }
        val interestRate = remember{mutableStateOf(0)}
        DropdownMenu(expanded = expanded,
            onDismissRequest = {expanded = false}) {
            DropdownMenuItem(
                onClick = {interestRate.value = 15},
                text = { Text("15%") }
            )
            DropdownMenuItem(
                onClick = {interestRate.value = 10},
                text = { Text("10%") }
            )
            DropdownMenuItem(
                onClick = {interestRate.value = 5},
                text = { Text("5%") }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SecondScreenContentPreview() {
    PracticeTheme {
        SecondScreenContent()
    }
}