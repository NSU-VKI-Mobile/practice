package ci.nsu.moble.main.data.model

import androidx.compose.ui.graphics.Color

data class ColorItem(
    val id: Int,
    override val name: String,
    override val color: Color
) : Colorable