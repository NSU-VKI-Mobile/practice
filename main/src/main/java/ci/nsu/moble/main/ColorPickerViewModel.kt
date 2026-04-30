package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
) {
    // Вычисляемый цвет
    val color: Color get() = Color(red, green, blue)

    // Текстовое представление HEX
    val hexCode: String get() = "#%02X%02X%02X".format(red, green, blue)
}

class ColorPickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ColorUiState())
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()

    fun onRedChanged(newValue: Float) {
        _uiState.update { currentState ->
            currentState.copy(red = newValue.toInt())
        }
    }

    fun onGreenChanged(newValue: Float) {
        _uiState.update { currentState ->
            currentState.copy(green = newValue.toInt())
        }
    }

    fun onBlueChanged(newValue: Float) {
        _uiState.update { currentState ->
            currentState.copy(blue = newValue.toInt())
        }
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