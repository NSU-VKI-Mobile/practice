package ci.nsu.moble.main.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import ci.nsu.moble.main.data.model.ColorItem
import ci.nsu.moble.main.data.repository.ColorsProvider

private fun Map<String, Color>.mapIndexedToColorItems(): List<ColorItem> {
    return this.entries.mapIndexed { index, (name, color) ->
        ColorItem(index, name, color)
    }
}

class ColorsFinderVM : ViewModel() {
    private val colorsMap = ColorsProvider.getAllColors()

    fun getColorItems(): List<ColorItem> {
        return colorsMap.mapIndexedToColorItems()
    }

    fun findColorByName(name: String): Color? {
        return ColorsProvider.getColor(name)
    }
}