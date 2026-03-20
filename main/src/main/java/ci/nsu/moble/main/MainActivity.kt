package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ColorScreenPreview() {
    ColorScreen()
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorScreen()
        }
    }
}


object ColorRepository {

    private val colors = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "orange" to Color(0xFFFFA500),
        "indigo" to Color(0xFF4B0082),
        "violet" to Color(0xFF8F00FF)

    )

    fun findColor(name: String): Color? {
        return colors[name.lowercase()]
    }

    fun getAll(): Map<String, Color> = colors
}


@Composable
fun ColorScreen() {

    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Green) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val color = ColorRepository.findColor(text.trim())

                if (color != null) {
                    buttonColor = color
                } else {
                    Log.d("ColorSearch", "Цвет \"$text\" не найден")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить цвет", fontSize = 16.sp)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ColorRepository.getAll().toList()) { (name, color) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = name.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White
                    )
                }
            }
        }
    }

}