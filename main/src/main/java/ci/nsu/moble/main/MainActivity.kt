package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.*

private val colorsMap = mapOf(
    "Red" to Red1,
    "Orange" to Orange2,
    "Yellow" to Yellow3,
    "Green" to Green4,
    "Light_Blue" to Light_Blue5,
    "Blue" to Blue6,
    "Violet" to Violet7
)

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

@Composable
fun Main(modifier: Modifier = Modifier) {
    var textColor by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

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
                    val foundColor = colorsMap[colorName]

                    if (foundColor != null) {
                        selectedColor = foundColor
                    } else {
                        selectedColor = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBg,
                    contentColor = contentColor
                )
            ) {
                Text("Применить цвет")
            }

            ColorPalettePreview(colors = colorsMap)
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
