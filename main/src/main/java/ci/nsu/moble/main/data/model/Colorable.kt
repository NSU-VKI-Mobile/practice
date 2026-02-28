package ci.nsu.moble.main.data.model

import androidx.compose.ui.graphics.Color

interface Colorable {
    val name: String
    val color: Color

    val colorInfo: String
        get() = "$name\n${color.red} ${color.green} ${color.blue}"
}