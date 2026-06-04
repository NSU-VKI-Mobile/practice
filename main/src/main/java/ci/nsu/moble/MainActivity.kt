package ci.nsu.moble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ci.nsu.moble.ui.theme.PracticeTheme

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
)

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsius = newValue
            val fahrenheit = if (celsius.isNotBlank()) {
                val c = celsius.toDoubleOrNull()
                if (c != null) String.format("%.2f", c * 9 / 5 + 32)
                else ""
            } else ""

            currentState.copy(
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }

    fun onFahrenheitChanged(newValue: String) {
        _uiState.update { currentState ->
            val fahrenheit = newValue
            val celsius = if (fahrenheit.isNotBlank()) {
                val f = fahrenheit.toDoubleOrNull()
                if (f != null) String.format("%.2f", (f - 32) * 5 / 9)
                else ""
            } else ""

            currentState.copy(celsius = celsius, fahrenheit = fahrenheit)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ValidateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val isValid = value.toDoubleOrNull() != null || value.isBlank()

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
            focusedIndicatorColor = if (isValid)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error,
            unfocusedIndicatorColor = if (isValid)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.error
        ),
        isError = !isValid && value.isNotBlank(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun MyScreen(
    viewModel: TemperatureViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ValidateTextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = "Градусы Цельсия"
        )
        ValidateTextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = "Градусы Фаренгейта"
        )
    }
}