package ci.nsu.moble.main.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Используется StateFlow для UiState
data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
) {
    val color: Color get() = Color(red, green, blue)

    // Форматирование HEX для корректного отображения
    val hexCode: String get() = String.format("#%02X%02X%02X", red, green, blue)
}

class ColorPickerViewModel : ViewModel() {
    // Приватный MutableStateFlow для изменения внутри
    private val _uiState = MutableStateFlow(ColorUiState())
    // Публичный StateFlow для чтения в UI
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()

    // Нет прямого изменения UI (только обновление данных состояния)
    fun onRedChanged(newValue: Float) {
        _uiState.update { it.copy(red = newValue.toInt()) }
    }

    fun onGreenChanged(newValue: Float) {
        _uiState.update { it.copy(green = newValue.toInt()) }
    }

    fun onBlueChanged(newValue: Float) {
        _uiState.update { it.copy(blue = newValue.toInt()) }
    }

    fun generateRandomColor() {
        _uiState.update {
            ColorUiState(
                red = (0..255).random(),
                green = (0..255).random(),
                blue = (0..255).random()
            )
        }
    }
}