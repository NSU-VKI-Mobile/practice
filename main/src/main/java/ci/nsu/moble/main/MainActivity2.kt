package ci.nsu.moble.main

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    ColorChanger(
                        name = "AAAA",
                        blank = ""
                    )
                }
            }
        }
    }
}

@Composable
fun ColorChanger(name: String, blank: String) {
    // 1. Создаём состояние (запоминается между перерисовками)
    var isRed by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("")}

        Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 2. Читаем значение — Compose подписывается на изменения
        Text(
            text = name,
            color = if (isRed) Color.Red else Color.Black
        )

        // 3. Меняем значение — Compose автоматически перерисовывает
        Button(onClick = { isRed = !isRed; }, colors = ButtonDefaults.buttonColors(
            containerColor = if (isRed) Color.Red else Color.Blue  // ← Цвет зависит от состояния
        )
        ) {  // ← Триггер перерисовки
            Text(
                text = if (isRed) text else blank,
                color = if (isRed) Color.Blue else Color.Black
                )
        }
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Введите текст") }
        )
    }
}