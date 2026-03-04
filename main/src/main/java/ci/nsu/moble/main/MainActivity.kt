package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

data class PaletteItem(val name: String, val color: Color)

private val paletteList = listOf(
    PaletteItem("Red", Color(0xFFFF0000)),
    PaletteItem("Orange", Color(0xFFFFA500)),
    PaletteItem("Yellow", Color(0xFFFFFF00)),
    PaletteItem("Green", Color(0xFF00FF00)),
    PaletteItem("Blue", Color(0xFF0000FF)),
    PaletteItem("Indigo", Color(0xFF4B0082)),
    PaletteItem("Violet", Color(0xFF8A2BE2)),
)

private val colorsMap: Map<String, Color> =
    paletteList.associate { it.name.trim().lowercase() to it.color }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PracticeTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFF00FF00)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val key = inputText.trim().lowercase()
                val found = colorsMap[key]
                if (found != null) {
                    buttonColor = found
                    Log.i("task2","Цвет изменен на \"${inputText.trim().lowercase()}\"")
                } else {
                    Log.i("Task2", "Пользовательский цвет \"$inputText\" не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("Применить цвет")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(paletteList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(item.color, RoundedCornerShape(15.dp))
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.name, color = Color.White)
                }
            }
        }
    }
}