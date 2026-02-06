package com.example.colorfinder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                ColorApp()
            }
        }
    }
}

data class ColorItem(val name: String, val color: Color)

val colors = listOf(
    ColorItem("Red", Color.Red),
    ColorItem("Orange", Color(0xFFFF9800)),
    ColorItem("Yellow", Color.Yellow),
    ColorItem("Green", Color.Green),
    ColorItem("Blue", Color.Blue),
    ColorItem("Indigo", Color(0xFF4B0082)),
    ColorItem("Violet", Color(0xFF8F00FF))
)

@Composable
fun ColorApp() {
    var text by remember { mutableStateOf("") }
    var buttonBg by remember { mutableStateOf(Color.LightGray) }

    Column {
        // Поле ввода
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка
        Button(
            onClick = {
                val found = colors.find {
                    it.name.equals(text.trim(), ignoreCase = true)
                }
                if (found != null) {
                    buttonBg = found.color
                } else {
                    Log.d("MY_APP", "Цвет '$text' не найден")
                    buttonBg = Color.LightGray
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = buttonBg
            )
        ) {
            Text("Применить цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Список цветов
        Text("Палитра:", fontSize = 20.sp)

        LazyColumn {
            items(colors) { color ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable {
                            buttonBg = color.color
                            text = color.name
                        }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(color.name, fontSize = 18.sp)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color.color, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}