package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.widget.ArrayAdapter
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ColorAdapter(
    context: Context,
    private val colorsMap: Map<String, Int>
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, colorsMap.keys.toList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view as TextView

        val colorName = getItem(position)
        val colorValue = colorsMap[colorName] ?: Color.BLACK

        textView.setBackgroundColor(colorValue)
        textView.setTextColor(Color.BLACK)
        textView.text = colorName

        return view
    }
}

class MainActivity : AppCompatActivity() {

    private val colorsMap = mapOf(
        "Red" to Color.RED,
        "Cyan" to Color.CYAN,
        "Yellow" to Color.YELLOW,
        "Green" to Color.GREEN,
        "Blue" to Color.BLUE,
        "Magenta" to Color.MAGENTA,
        "Black" to Color.BLACK
    )

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var colorListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupColorList()
        setupSearchButton()
    }

    private fun initViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        colorListView = findViewById(R.id.colorListView)
    }

    private fun setupColorList() {
        val adapter = ColorAdapter(this, colorsMap)
        colorListView.adapter = adapter

        // Обработка нажатия на элемент списка
        colorListView.setOnItemClickListener { _, _, position, _ ->
            val colorName = colorsMap.keys.toList()[position]
            val colorValue = colorsMap[colorName]

            colorValue?.let {
                searchButton.setBackgroundColor(it)
                // Автоматический выбор цвета текста для читаемости
                val buttonTextColor = if (isColorDark(it)) Color.WHITE else Color.BLACK
                searchButton.setTextColor(buttonTextColor)

                Toast.makeText(this, "Цвет '$colorName' применен к кнопке", Toast.LENGTH_SHORT).show()
                Log.i("ColorSearch", "Цвет '$colorName' применен к кнопке из списка")
            }
        }
    }

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val userInput = searchEditText.text.toString().trim()

            if (userInput.isEmpty()) {
                Toast.makeText(this, "Введите название цвета", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ищем цвет в структуре данных
            val foundColor = findColor(userInput)

            if (foundColor != null) {
                // Цвет найден - меняем фон кнопки
                searchButton.setBackgroundColor(foundColor)

                // Меняем цвет текста для читаемости
                val buttonTextColor = if (isColorDark(foundColor)) Color.WHITE else Color.BLACK
                searchButton.setTextColor(buttonTextColor)

                Toast.makeText(this, "Цвет '$userInput' найден!", Toast.LENGTH_SHORT).show()
                Log.i("ColorSearch", "Пользовательский цвет '$userInput' найден и применен к кнопке")
            } else {
                // Цвет не найден - кнопка остается неизменной
                Log.w("ColorSearch", "Пользовательский цвет '$userInput' не найден")
                Toast.makeText(
                    this,
                    "Цвет '$userInput' не найден",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Функция поиска цвета по названию
    private fun findColor(colorName: String): Int? {
        // Ищем точное совпадение
        for ((key, value) in colorsMap) {
            if (key.equals(colorName, ignoreCase = true)) {
                return value
            }
        }
        return null
    }

    // Функция определения темный цвет или светлый
    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) +
                0.587 * Color.green(color) +
                0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}