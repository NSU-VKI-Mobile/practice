package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ci.nsu.moble.main.ui.theme.MobilepracticeTheme
import androidx.compose.material3.*
import ci.nsu.moble.main.ui.ColorPickerScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Content goes under status bar
        setContent {
            MobilepracticeTheme {
                // Контейнер, использующий цвет фона из темы
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Запуск вашего основного экрана
                    ColorPickerScreen()
                }
            }
        }
    }
}
