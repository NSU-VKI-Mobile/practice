package ci.nsu.moble.main.data.repository

import androidx.compose.ui.graphics.Color
import ci.nsu.moble.main.ui.theme.*

object ColorsProvider {
    private val colorsMap = mapOf(
        "red" to Red,
        "orange" to Orange,
        "yellow" to Yellow,
        "green" to Green,
        "blue" to Blue,
        "indigo" to Indigo,
        "violet" to Violet
    )

    fun getColor(name: String): Color? {
        return colorsMap[name.lowercase()]
    }

    fun getAllColors(): Map<String, Color> {
        return colorsMap
    }
}