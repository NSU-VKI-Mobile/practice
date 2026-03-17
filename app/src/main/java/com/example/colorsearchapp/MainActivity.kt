package com.example.colorsearchapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etColorInput: EditText
    private lateinit var btnApplyColor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etColorInput = findViewById(R.id.etColorInput)
        btnApplyColor = findViewById(R.id.btnApplyColor)

        btnApplyColor.setOnClickListener {
            // Получаем введенный текст и приводим к нижнему регистру
            val inputColor = etColorInput.text.toString().trim().lowercase()

            // Ищем цвет
            val foundColor = ColorData.colors[inputColor]

            if (foundColor != null) {
                // красим кнопку
                btnApplyColor.setBackgroundColor(foundColor)
                Log.i("ColorSearch", "Цвет '$inputColor' найден и применен")
            } else {
                // Цвет не найден
                btnApplyColor.setBackgroundColor(Color.parseColor("#2196F3"))
                Log.w("ColorSearch", "Цвет '$inputColor' не найден в палитре")
            }
        }
    }
}