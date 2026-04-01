package ci.nsu.mobile.main

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PracticeTheme {
                ColorPickerScreen()
            }
        }
    }
}

val colorMap = mapOf(
    "red" to Color.Red,
    "orange" to Color(0xFFFF9800),
    "yellow" to Color.Yellow,
    "green" to Color.Green,
    "blue" to Color.Blue,
    "indigo" to Color(0xFF3F51B5),
    "violet" to Color(0xFF9C27B0),
    "pink" to Color(0xFFE91E63),
    "cyan" to Color.Cyan,
    "lime" to Color(0xFFCDDC39),
)

@Composable
fun ColorPickerScreen() {
    var inputColorName by remember { mutableStateOf("") }
    var buttonBackground by remember { mutableStateOf(Color(0xFF4CAF50)) }

    val currentColorName = remember { mutableStateOf("Green") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выбранный цвет: ${currentColorName.value}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = inputColorName,
            onValueChange = { inputColorName = it },
            label = { Text("Название цвета (англ.)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        Button(
            onClick = {
                val colorName = inputColorName.trim()
                val foundColor = colorMap[colorName]

                if (foundColor != null) {
                    buttonBackground = foundColor
                    currentColorName.value = colorName
                    Log.d("ColorPicker", "Цвет успешно применён: $colorName")
                } else {
                    Log.d("ColorPicker", "Пользовательский цвет \"$colorName\" не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBackground),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Применить цвет",
                color = if (buttonBackground == Color.White || buttonBackground == Color.Yellow)
                    Color.Black else Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Доступные цвета в палитре:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Start
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colorMap.toList()) { (name, color) ->
                ColorItem(name = name, color = color)
            }
        }
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            color = if (color == Color.Yellow || color == Color(0xFFCDDC39))
                Color.Black else Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ColorPickerScreenPreview() {
    PracticeTheme {
        ColorPickerScreen()
    }
}