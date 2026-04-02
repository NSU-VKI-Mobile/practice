package ci.nsu.mobile.main

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme { ColorPickerScreen() }
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
    "lime" to Color(0xFFCDDC39)
)
val darkTextColors = setOf(Color.Green,Color.Yellow,Color.Blue)

@Composable
fun ColorPickerScreen() {
    var input by remember { mutableStateOf("") }
    var buttonBg by remember { mutableStateOf(Color.Green) }
    var selected by remember { mutableStateOf("Green") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Название цвета (англ.)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                input.trim().let { name ->
                    colorMap[name]?.let { color ->
                        buttonBg = color
                        selected = name
                        Log.d("ColorPicker", "Цвет успешно применён: $name")
                    } ?: Log.d("ColorPicker", "Цвет \"$name\" не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBg),
        ) {
            Text(
                "Применить цвет",
                color = if (buttonBg == Color.White || buttonBg == Color.Yellow) Color.Black else Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(32.dp))

        Text("Доступные цвета в палитре:", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(colorMap.toList()) { (name, color) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        name,
                        color = if(color in darkTextColors) Color.Black else Color.White )

                  }

                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun Preview() {
    PracticeTheme { ColorPickerScreen() }
}