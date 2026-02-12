package ci.nsu.moble.main.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.ui.theme.*

private val colorsMap = mapOf(
    "Red" to Red1,
    "Blue" to Blue1,
    "Green" to Green1,
    "Orange" to Orange1,
    "Yellow" to Yellow1,
    "Purple" to Purple1,
    "Light Blue" to LightBlue1,
    "Black" to Black1,
    "White" to White1
)

class Task2 : ComponentActivity() {
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
fun Main(modifier: Modifier = Modifier) {
    var textColor by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    @Composable
    fun ColorPalettePreview(
        colors: Map<String, Color>,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Доступные цвета:", style = MaterialTheme.typography.titleMedium)

            colors.forEach { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        color = if (color.luminance() > 0.5) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = textColor,
                onValueChange = { newText -> textColor = newText },
                label = { Text("Введите цвет") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            val buttonBg = selectedColor ?: MaterialTheme.colorScheme.primary
            val contentColor = if (buttonBg.luminance() > 0.5) Color.Black else Color.White
            Button(
                onClick = {
                    val colorName = textColor.trim()

                    if (colorName.isEmpty()) {
                        selectedColor = null
                        return@Button
                    }

                    val foundColor = colorsMap[colorName]

                    if (foundColor != null) {
                        selectedColor = foundColor
                        println("Цвет '$colorName' найден: $foundColor")
                    } else {
                        selectedColor = null
                        android.util.Log.w("ColorSearch", "Цвет '$colorName' не найден")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBg, contentColor = contentColor)
            ) {
                Text("Применить цвет")
            }

            ColorPalettePreview(
                colors = colorsMap
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Main()
    }
}