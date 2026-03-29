package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.TemperatureUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsius = newValue
            val fahrenheit = if (celsius.isNotBlank()) {
                val c = celsius.toDoubleOrNull()
                if (c != null) {
                    String.format("%.2f", c * 9/5 + 32)
                } else {
                    "" // Невалидное значение
                }
            } else {
                "" // Пустое поле
            }

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
                if (f != null) {
                    String.format("%.2f", (f - 32) * 5/9)
                } else {
                    "" // Невалидное значение
                }
            } else {
                "" // Пустое поле
            }

            currentState.copy(
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }
}