package ci.nsu.mobile.main

import androidx.compose.ui.graphics.Color

data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128)
{
    // Вычисляемый цвет для отображения
    val color: Color get() = Color(
        red = red / 255f,
        green = green / 255f,
        blue = blue / 255f
    )


    val hexCode: String get() = "#${red.toString(16).padStart(2, '0')}${green.toString(16).padStart(2, '0')}${blue.toString(16).padStart(2, '0')}".uppercase()

// Яркость
    val luminance: Float get() = (0.299f * red + 0.587f * green + 0.114f * blue) / 255f
    val textColor: Color get() = if (luminance > 0.5f) Color.Black else Color.White
}