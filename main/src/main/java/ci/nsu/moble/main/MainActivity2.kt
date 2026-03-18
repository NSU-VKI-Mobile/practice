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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorSearchScreen()
                }
            }
        }
    }
}

data class ColorInfo(
    val name: String,
    val color: Color
)

object ColorData {
    private val colorList = listOf(
        ColorInfo("red", Color.Red),
        ColorInfo("blue", Color.Blue),
        ColorInfo("green", Color.Green),
        ColorInfo("yellow", Color.Yellow),
        ColorInfo("purple", Color(0xFF9C27B0)),
        ColorInfo("orange", Color(0xFFFF9800)),
        ColorInfo("pink", Color(0xFFE91E63)),
        ColorInfo("black", Color.Black),
        ColorInfo("white", Color.White),
        ColorInfo("gray", Color.Gray)
    )

    fun searchColor(query: String): Color? {
        return colorList.find { it.name.equals(query, ignoreCase = true) }?.color
    }

    fun getAllColors(): List<ColorInfo> = colorList
}

@Composable
fun ColorSearchScreen() {
    var inputText by remember { mutableStateOf("") }
    var buttonBgColor by remember { mutableStateOf(Color.LightGray) }
    val allColors = remember { ColorData.getAllColors() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Поиск цвета",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val foundColor = ColorData.searchColor(inputText)
                if (foundColor != null) {
                    buttonBgColor = foundColor
                    Log.d("ColorTag", "Найден цвет: $inputText")
                } else {
                    Log.d("ColorTag", "Цвет не найден: $inputText")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBgColor
            )
        ) {
            Text(
                text = "Найти",
                color = if (buttonBgColor == Color.Black ||
                    buttonBgColor == Color.Blue)
                    Color.White
                else
                    Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Простая линия-разделитель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Все доступные цвета:",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(allColors) { color ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = color.color
                    )
                ) {
                    Text(
                        text = color.name,
                        modifier = Modifier.padding(16.dp),
                        color = if (color.color == Color.Black ||
                            color.color == Color.Blue)
                            Color.White
                        else
                            Color.Black
                    )
                }
            }
        }
    }
}