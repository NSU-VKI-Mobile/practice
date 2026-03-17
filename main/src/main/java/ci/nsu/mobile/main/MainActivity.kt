package ci.nsu.mobile.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val colors = mapOf(
        "Red" to Color.Red,
        "Orange" to Color(0xFFFFA500),
        "Yellow" to Color.Yellow,
        "Green" to Color.Green,
        "Blue" to Color.Blue,
        "Indigo" to Color(0xFF4B0082),
        "Violet" to Color(0xFF8F00FF)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ColorScreen(colors) }
    }
}

@Composable
fun ColorScreen(colors: Map<String, Color>) {
    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFFACAFB0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val normalizedText = text.trim().lowercase()

                val color = colors.entries
                    .find { it.key.lowercase() == normalizedText }
                    ?.value

                if (color != null) {
                    buttonColor = color
                } else {
                    Log.d("COLOR_LOG", "Цвет \"$text\" не найден")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("Применить цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        colors.forEach { (name, color) ->
            ColorItem(name, color)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun ColorItem(name: String, color: Color) {
    Row {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = name)
    }
}


