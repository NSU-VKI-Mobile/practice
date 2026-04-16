package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TemperatureUiState( //хранит всё состояние экрана
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    // Вычисляемые свойства для валидации
    //Проверяют, можно ли преобразовать строку в число
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState()) //Хранит текущее состояние
                //TemperatureUiState() — создаёт начальное состояние (пустые строки).
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow() //Публичный неизменяемый поток
    //View может подписаться на изменения, но не может изменять данные напрямую
    //asStateFlow() — превращает MutableStateFlow в StateFlow (убирает методы изменения)

    fun onCelsiusChanged(newValue: String) { //вызывается, когда пользователь меняет поле Цельсия (обрабатывает ввод и обновляет состояние)
        _uiState.update { currentState -> //безопасное обновление состояния; текузее состояние, до изменения
            val celsius = newValue //сохранение нового состояния
            val fahrenheit = if (celsius.isNotBlank()) { //не пустая строка
                val c = celsius.toDoubleOrNull()
                if (c != null) String.format("%.2f", c * 9/5 + 32)
                else "" //если не число
            } else "" //если строка пустая

            currentState.copy( //Создаём НОВЫЙ объект состояния на основе старого
                celsius = celsius, //неизменяемость данных - старый объект не меняется
                fahrenheit = fahrenheit //copy() — автоматически генерируется для data class
            )
        }
    }

    fun onFahrenheitChanged(newValue: String) {
        // TODO: реализовать обратный расчет
        _uiState.update { currentState ->
            val fahrenheit = newValue
            val celsius = if (fahrenheit.isNotBlank()) {
                val f = fahrenheit.toDoubleOrNull()
                if (f != null) String.format("%.2f", (f - 32) * 5/9)
                else ""
            } else ""

            currentState.copy(
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }

    fun clearFields() {
        _uiState.update { TemperatureUiState() } //Возвращает состояние к начальному (пустые строки)
    }
}