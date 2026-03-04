package com.example.laba_2android_studio

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class ColorSearchFragment : Fragment() {
    private val availableColors = listOf(
        ColorItem("Red", Color.RED),
        ColorItem("Orange", Color.rgb(255, 165, 0)), // Оранжевый
        ColorItem("Yellow", Color.YELLOW),
        ColorItem("Green", Color.GREEN),
        ColorItem("Blue", Color.BLUE),
        ColorItem("Indigo", Color.rgb(75, 0, 130)), // Индиго
        ColorItem("Violet", Color.rgb(238, 130, 238)), // Фиолетовый
        ColorItem("Gray", Color.GRAY)
    )

    private lateinit var searchButton: Button
    private lateinit var colorEditText: EditText
    private lateinit var paletteContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton = view.findViewById(R.id.searchButton)
        colorEditText = view.findViewById(R.id.colorEditText)
        paletteContainer = view.findViewById(R.id.paletteContainer)

        searchButton.setOnClickListener {
            searchColor()
        }

        displayColorPalette()
    }

    private fun searchColor() {
        val query = colorEditText.text.toString().trim()

        if (query.isEmpty()) {
            return
        }

        val foundColor = availableColors.find {
            it.name.equals(query, ignoreCase = true)
        }

        if (foundColor != null) {
            searchButton.setBackgroundColor(foundColor.color)
            if (isColorDark(foundColor.color)) {
                searchButton.setTextColor(Color.WHITE)
            } else {
                searchButton.setTextColor(Color.BLACK)
            }
        } else {
            Log.d("ColorSearch", "Пользовательский цвет \"$query\" не найден")
        }
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    private fun displayColorPalette() {
        paletteContainer.removeAllViews()

        availableColors.forEach { colorItem ->
            val colorView = LayoutInflater.from(context).inflate(R.layout.item_color, paletteContainer, false)

            val colorBlock: View = colorView.findViewById(R.id.colorBlock)
            val colorNameText: TextView = colorView.findViewById(R.id.colorNameText)
            val colorHexText: TextView = colorView.findViewById(R.id.colorHexText)

            colorBlock.setBackgroundColor(colorItem.color)

            colorNameText.text = colorItem.name

            val hexColor = String.format("#%06X", 0xFFFFFF and colorItem.color)
            colorHexText.text = hexColor

            val textColor = if (isColorDark(colorItem.color)) Color.WHITE else Color.BLACK
            colorNameText.setTextColor(textColor)
            colorHexText.setTextColor(textColor)

            paletteContainer.addView(colorView)
        }
    }
}