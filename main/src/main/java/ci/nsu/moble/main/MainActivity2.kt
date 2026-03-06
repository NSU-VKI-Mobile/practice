package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var text by remember { mutableStateOf("Green") }
    var btnColor by remember { mutableStateOf(Color.Green) }


    val colorsMap = mapOf(
        "red" to Color(0xFFFF0000),
        "orange" to Color(0xFFFFA500),
        "yellow" to Color(0xFFFFFF00),
        "green" to Color(0xFF00FF00),
        "blue" to Color(0xFF0000FF),
        "indigo" to Color(0xFF4B0082),
        "violet" to Color(0xFFEE82EE)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Название цвета") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val colorName = text.trim().lowercase()
                val newColor = colorsMap[colorName]

                if (newColor != null) {
                    btnColor = newColor
                } else {
                    Log.e("ColorApp", "Такого цвета нет: $text")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
        ) {
            Text(
                text = "Применить цвет", color = getContrastColor(btnColor)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text("Примеры цветов", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            colorsMap.forEach { (name, color) ->
                ColorRow(name, color)
            }
        }
    }
}

@Composable
fun ColorRow(name: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = name,
            color = getContrastColor(color),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun getContrastColor(background: Color): Color {
    val luminance = background.red + background.green + background.blue
    return if (luminance > 1.5f) Color.Black else Color.White
}