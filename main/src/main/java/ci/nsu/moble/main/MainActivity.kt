// SimpleMainActivity.kt
package com.example.colorsearch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SimpleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleColorSearchScreen()
        }
    }
}

// Простая модель данных
data class SimpleColor(
    val name: String,
    val color: Color
)

// Простой репозиторий
object SimpleColorRepository {
    private val colors = listOf(
        SimpleColor("red", Color.Red),
        SimpleColor("blue", Color.Blue),
        SimpleColor("green", Color.Green),
        SimpleColor("yellow", Color.Yellow),
        SimpleColor("purple", Color(0xFF9C27B0)),
        SimpleColor("orange", Color(0xFFFF9800)),
        SimpleColor("pink", Color(0xFFE91E63)),
        SimpleColor("black", Color.Black),
        SimpleColor("white", Color.White),
        SimpleColor("gray", Color.Gray)
    )

    fun findColor(name: String): Color? {
        return colors.find { it.name == name.lowercase() }?.color
    }

    fun getAllColors(): List<SimpleColor> = colors
}

@Composable
fun SimpleColorSearchScreen() {
    var text by remember { mutableStateOf("") }
    var buttonBg by remember { mutableStateOf(Color.LightGray) }
    val allColors = remember { SimpleColorRepository.getAllColors() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Поиск цвета", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val found = SimpleColorRepository.findColor(text)
                if (found != null) {
                    buttonBg = found
                    Log.d("ColorSearch", "Найден цвет: $text")
                } else {
                    Log.d("ColorSearch", "Не найден цвет: $text")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBg
            )
        ) {
            Text("Поиск", color = if (buttonBg == Color.Black) Color.White else Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Палитра цветов", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn {
            items(allColors) { colorItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorItem.color
                    )
                ) {
                    Text(
                        text = colorItem.name,
                        modifier = Modifier.padding(16.dp),
                        color = if (colorItem.color == Color.Black ||
                            colorItem.color == Color.Blue)
                            Color.White else Color.Black
                    )
                }
            }
        }
    }
}