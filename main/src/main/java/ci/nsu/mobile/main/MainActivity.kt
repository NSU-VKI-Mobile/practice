package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureConverterScreen()
        }
    }
}

@Composable
fun TemperatureConverterScreen(
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🌡️ Конвертер температуры",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Поле для Цельсия
        OutlinedTextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = { Text("Градусы Цельсия (°C)") },
            placeholder = { Text("Например: 25") },
            isError = uiState.celsius.isNotBlank() && !uiState.isCelsiusValid,
            supportingText = {
                if (uiState.celsius.isNotBlank() && !uiState.isCelsiusValid) {
                    Text("Введите число", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "⬇️  Равно  ⬇️",
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для Фаренгейта
        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Градусы Фаренгейта (°F)") },
            placeholder = { Text("Например: 77") },
            isError = uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid,
            supportingText = {
                if (uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid) {
                    Text("Введите число", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.clearFields() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("🗑️ Очистить всё")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Карточка с формулами
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📐 Формулы:", style = MaterialTheme.typography.titleSmall)
                Text("°F = (°C × 9/5) + 32", style = MaterialTheme.typography.bodyMedium)
                Text("°C = (°F - 32) × 5/9", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🔄 Поверните экран - данные сохранятся!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}