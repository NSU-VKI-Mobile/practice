package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TemperatureUiState(//здесь лежат текста
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()//данные которые отображаются пользователю и программа не может их менять

    fun onCelsiusChanged(newValue: String) {//меняется каждый раз когда мы меняем строку цельсия
        _uiState.update { currentState ->
            val celsius = newValue
            val fahrenheit = if (celsius.isNotBlank()) {
                val c = celsius.toDoubleOrNull()//преобразовываем текст в число
                if (c != null) {
                    String.format("%.2f", c * 9/5 + 32)//формула- округление
                } else ""
            } else ""

            currentState.copy(//создаем новую коробочку с данными
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }

    fun onFahrenheitChanged(newValue: String) {//когда пользователь что нибудь печатает то вызывается это переменная
        _uiState.update { currentState ->
            val fahrenheit = newValue//сохраняем данные введенные пользователем
            val celsius = if (fahrenheit.isNotBlank()) {//ечли поле не пустое считаем цельсий
                val f = fahrenheit.toDoubleOrNull()
                if (f != null) {
                    String.format("%.2f", (f - 32) * 5/9)
                } else ""
            } else ""

            currentState.copy(
                celsius = celsius,// меняем сохраняем и показываем на экран новые значения
                fahrenheit = fahrenheit
            )
        }
    }
    fun clearFields() {
        _uiState.update {
            TemperatureUiState() // Очищаем оба поля
        }
    }
}