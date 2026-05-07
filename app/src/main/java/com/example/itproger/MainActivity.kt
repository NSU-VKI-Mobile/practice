package com.example.itproger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

// MainActivity — точка входа в приложение. Android запускает его первым.
// ComponentActivity — базовый класс для Activity с поддержкой Compose.
class MainActivity : ComponentActivity() {

    // onCreate вызывается когда Activity создаётся (запуск приложения или поворот экрана)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // контент рисуется под системными барами (статусбар, навигация)
        setContent {
            // MaterialTheme — оборачивает всё приложение стилями (цвета, шрифты, формы)
            MaterialTheme {
                // Surface — просто фоновый прямоугольник с правильным цветом фона из темы
                Surface {
                    CounterScreen() // запускаем наш экран
                }
            }
        }
    }
}