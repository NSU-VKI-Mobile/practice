package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ColorPickerViewModel : ViewModel() {
    // Приватный изменяемый поток состояния
    private val _uiState = MutableStateFlow(ColorUiState())

    // Публичный неизменяемый поток для UI
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()

    // Update красного компонента
    fun onRedChanged(newValue: Float) {
        _uiState.update { it.copy(red = newValue.toInt()) }
    }

    // Update зеленого компонента
    fun onGreenChanged(newValue: Float) {
        _uiState.update { it.copy(green = newValue.toInt()) }
    }

    // Update синего компонента
    fun onBlueChanged(newValue: Float) {
        _uiState.update { it.copy(blue = newValue.toInt()) }
    }

    // Ген случайного цвета
    fun generateRandomColor() {
        _uiState.update {
            ColorUiState(
                red = Random.nextInt(0, 256),
                green = Random.nextInt(0, 256),
                blue = Random.nextInt(0, 256)
            )
        }
    }
}