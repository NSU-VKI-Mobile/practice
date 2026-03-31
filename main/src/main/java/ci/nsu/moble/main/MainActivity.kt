package ci.nsu.moble.main

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Тема (можно создать свою или использовать пустую)
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TemperatureScreen()
                }
            }
        }
    }
}

@Composable
fun TemperatureScreen(viewModel: TemperatureViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Celsius input
        OutlinedTextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = { Text("Celsius") },
            isError = !uiState.isCelsiusValid && uiState.celsius.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!uiState.isCelsiusValid && uiState.celsius.isNotBlank()) {
            Text(
                text = "Invalid Celsius value",
                color = MaterialTheme.colorScheme.error
            )
        }

        // Fahrenheit input
        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Fahrenheit") },
            isError = !uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank()) {
            Text(
                text = "Invalid Fahrenheit value",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}