package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color

class ColorHash {
    val colorMap: Map<String, Color> = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
        "black" to Color.Black,
        "white" to Color.White,
        "gray" to Color.Gray,
        "darkgray" to Color.DarkGray,
        "lightgray" to Color.LightGray
    )

    fun getColor(colorName: String): Color {
        return colorMap[colorName.lowercase()] ?: Color.Gray
    }
}