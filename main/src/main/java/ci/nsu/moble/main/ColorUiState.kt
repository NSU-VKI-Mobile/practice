package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color

data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
) {
    // Вычисляемый цвет
    val color: Color get() = Color(red, green, blue)

    // Текстовое представление
    val hexCode: String get() = "#${red.toString(16)}${green.toString(16)}${blue.toString(16)}"
}