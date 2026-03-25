package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ConversionMode {
    CELSIUS_TO_FAHRENHEIT,  // Режим: Цельсий → Фаренгейт
    FAHRENHEIT_TO_CELSIUS   // Режим: Фаренгейт → Цельсий
}

data class TemperatureUiState(
    val inputValue: String = "",      // Значение в поле ввода (верхнее)
    val resultValue: String = "",      // Значение в поле результата (нижнее)
    val mode: ConversionMode = ConversionMode.CELSIUS_TO_FAHRENHEIT
) {
    val isInputValid: Boolean get() = inputValue.toDoubleOrNull() != null

    val inputHint: String
        get() = if (mode == ConversionMode.CELSIUS_TO_FAHRENHEIT)
            "Градусы Цельсия (°C)"
        else
            "Градусы Фаренгейта (°F)"

    val resultHint: String
        get() = if (mode == ConversionMode.CELSIUS_TO_FAHRENHEIT)
            "Градусы Фаренгейта (°F)"
        else
            "Градусы Цельсия (°C)"

    val buttonText: String
        get() = if (mode == ConversionMode.CELSIUS_TO_FAHRENHEIT)
            "Сменить на °F → °C"
        else
            "Сменить на °C → °F"

    val modeText: String
        get() = if (mode == ConversionMode.CELSIUS_TO_FAHRENHEIT)
            "Режим: °C → °F"
        else
            "Режим: °F → °C"
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun toggleMode() {
        _uiState.update { currentState ->
            val newMode = if (currentState.mode == ConversionMode.CELSIUS_TO_FAHRENHEIT) {
                ConversionMode.FAHRENHEIT_TO_CELSIUS
            } else {
                ConversionMode.CELSIUS_TO_FAHRENHEIT
            }

            // При смене режима очищаем поля
            currentState.copy(
                inputValue = "",
                resultValue = "",
                mode = newMode
            )
        }
    }

    fun onInputChanged(newValue: String) {
        _uiState.update { currentState ->
            val inputValue = newValue
            val resultValue = if (inputValue.isNotBlank()) {
                val value = inputValue.toDoubleOrNull()
                if (value != null) {
                    when (currentState.mode) {
                        ConversionMode.CELSIUS_TO_FAHRENHEIT -> {
                            // Цельсий → Фаренгейт
                            String.format("%.2f", value * 9/5 + 32)
                        }
                        ConversionMode.FAHRENHEIT_TO_CELSIUS -> {
                            // Фаренгейт → Цельсий
                            String.format("%.2f", (value - 32) * 5/9)
                        }
                    }
                } else {
                    "" // Невалидное значение
                }
            } else {
                "" // Пустое поле
            }

            currentState.copy(
                inputValue = inputValue,
                resultValue = resultValue
            )
        }
    }
}