package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UiState — хранит строки, чтобы пользователь мог вводить частичные значения (например, "-" или ".")
data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    // Вычисляемые свойства для валидации
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null || celsius.isEmpty()
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null || fahrenheit.isEmpty()
}

class TemperatureViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    // Пользователь вводит Цельсии → автоматически считаем Фаренгейты
    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val fahrenheit = if (newValue.isNotBlank()) {
                val c = newValue.toDoubleOrNull()
                if (c != null) String.format("%.2f", c * 9.0 / 5.0 + 32) else ""
            } else ""

            currentState.copy(
                celsius = newValue,
                fahrenheit = fahrenheit
            )
        }
    }

    // Пользователь вводит Фаренгейты → автоматически считаем Цельсии
    fun onFahrenheitChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsius = if (newValue.isNotBlank()) {
                val f = newValue.toDoubleOrNull()
                if (f != null) String.format("%.2f", (f - 32) * 5.0 / 9.0) else ""
            } else ""

            currentState.copy(
                fahrenheit = newValue,
                celsius = celsius
            )
        }
    }

    fun reset() {
        _uiState.update { TemperatureUiState() }
    }
}
