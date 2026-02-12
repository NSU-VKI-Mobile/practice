package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    private lateinit var colorInput: EditText
    private lateinit var searchButton: Button
    private lateinit var paletteContainer: LinearLayout

    // Простая структура данных с цветами
    private val colorMap = mapOf(
        "red" to R.color.red,
        "green" to R.color.green,
        "blue" to R.color.blue,
        "yellow" to R.color.yellow,
        "purpule" to R.color.purple,
        "orange" to R.color.orange,
        "pink" to R.color.pink,
        "black" to R.color.black,
        "white" to R.color.white,
        "gray" to R.color.gray
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем интерфейс программно (без XML)
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32)
        }

        // Заголовок
        mainLayout.addView(TextView(this).apply {
            text = "Поиск цвета"
            textSize = 24f
            setPadding(0, 0, 0, 32)
        })

        // Поле ввода
        colorInput = EditText(this).apply {
            hint = "Введите название цвета"
            setPadding(32)
        }
        mainLayout.addView(colorInput)

        // Кнопка поиска
        searchButton = Button(this).apply {
            text = "Найти цвет"
            setPadding(32)
            setOnClickListener { searchColor() }
        }
        mainLayout.addView(searchButton)

        // Заголовок палитры
        mainLayout.addView(TextView(this).apply {
            text = "Доступные цвета:"
            textSize = 18f
            setPadding(0, 32, 0, 16)
        })

        // Контейнер для палитры
        paletteContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        mainLayout.addView(paletteContainer)

        // Создаем цвета в палитре
        createPalette()

        setContentView(mainLayout)
    }

    private fun searchColor() {
        val query = colorInput.text.toString().trim().lowercase()

        if (query.isEmpty()) return

        val colorResId = colorMap[query]

        if (colorResId != null) {
            // Цвет найден - меняем фон кнопки
            searchButton.setBackgroundColor(ContextCompat.getColor(this, colorResId))
            Log.d("ColorSearch", "Цвет '$query' найден, применен к кнопке")
        } else {
            // Цвет не найден - кнопка не меняется (ставим серый цвет)
            searchButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            Log.w("ColorSearch", "Пользовательский цвет '$query' не найден")
        }
    }

    private fun createPalette() {
        for ((name, colorResId) in colorMap) {
            val colorButton = Button(this).apply {
                text = name
                setBackgroundColor(ContextCompat.getColor(this@MainActivity, colorResId))
                setPadding(16)

                // Устанавливаем цвет текста для читаемости
                setTextColor(if (name == "whitr" || name == "yellow")
                    ContextCompat.getColor(this@MainActivity, R.color.black)
                else
                    ContextCompat.getColor(this@MainActivity, R.color.white))

                // При клике на цвет в палитре - вставляем название в поле ввода
                setOnClickListener {
                    colorInput.setText(name)
                    searchColor()
                }
            }
            paletteContainer.addView(colorButton)

            // Добавляем отступ между кнопками
            (colorButton.layoutParams as? LinearLayout.LayoutParams)?.setMargins(0, 0, 0, 8)
        }
    }
}