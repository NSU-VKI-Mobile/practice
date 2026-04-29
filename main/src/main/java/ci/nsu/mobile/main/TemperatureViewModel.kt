package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    // Вычисляемые свойства для валидации
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsius = newValue
            val fahrenheit = if (celsius.isNotBlank()) {
                val c = celsius.toDoubleOrNull()
                if (c != null) String.format("%.2f", c * 9/5 + 32)
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
                if (f != null) String.format("%.2f", (f - 32) * 5.0 / 9.0)
                else ""
            } else ""

            currentState.copy(
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }
}