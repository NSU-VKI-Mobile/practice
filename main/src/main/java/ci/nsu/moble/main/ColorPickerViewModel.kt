package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ColorPickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ColorUiState())
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()

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