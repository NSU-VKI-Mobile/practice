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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureConverterApp()
        }
    }
}

@Composable
fun TemperatureConverterApp() {
    // Создаем ViewModel
    val viewModel: TemperatureViewModel = viewModel()

    // Подписываемся на изменения состояния
    val uiState by viewModel.uiState.collectAsState()

    // Отображаем экран
    TemperatureScreen(
        uiState = uiState,
        onCelsiusChanged = { viewModel.onCelsiusChanged(it) },
        onFahrenheitChanged = { viewModel.onFahrenheitChanged(it) }
    )
}

@Composable
fun TemperatureScreen(
    uiState: TemperatureUiState,
    onCelsiusChanged: (String) -> Unit,
    onFahrenheitChanged: (String) -> Unit
) {
    val isCelsiusError = !uiState.isCelsiusValid && uiState.celsius.isNotBlank()
    val isFahrenheitError = !uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Конвертер температуры",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        OutlinedTextField(
            value = uiState.celsius,
            onValueChange = onCelsiusChanged,
            label = { Text("Градусы Цельсия (°C)") },
            isError = isCelsiusError,
            supportingText = {
                if (isCelsiusError) {
                    Text("Введите число")
                }
            },

        )




        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = onFahrenheitChanged,
            label = { Text("Градусы Фаренгейта (°F)") },
            isError = isFahrenheitError,
            supportingText = {
                if (isFahrenheitError) {
                    Text("Введите число")
                }
            },

        )




    }
}