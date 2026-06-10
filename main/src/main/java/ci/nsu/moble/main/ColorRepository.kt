// Task_2: Хранилище цветов. Структура данных (Map) для поиска цвета по имени.

package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color

object ColorRepository {

    data class ColorEntry(val name: String, val color: Color)

    val colors = listOf(
        ColorEntry("red", Color(0xFFFF0000)),
        ColorEntry("orange", Color(0xFFFFA500)),
        ColorEntry("yellow", Color(0xFFFFFF00)),
        ColorEntry("green", Color(0xFF00FF00)),
        ColorEntry("blue", Color(0xFF0000FF)),
        ColorEntry("indigo", Color(0xFF4B0082)),
        ColorEntry("violet", Color(0xFF8F00FF))
    )

    private val colorMap = colors.associateBy { it.name.lowercase() }

    fun findColor(name: String): ColorEntry? {
        return colorMap[name.lowercase().trim()]
    }
}
