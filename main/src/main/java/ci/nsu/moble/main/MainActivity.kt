package ci.nsu.mobile.main

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
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Task2Screen()
                }
            }
        }
    }
}


val colorPalette = mapOf(
    "red" to Color.Red,
    "orange" to Color(0xFFFF9800),
    "yellow" to Color.Yellow,
    "green" to Color.Green,
    "blue" to Color.Blue,
    "indigo" to Color(0xFF3F51B5),
    "violet" to Color(0xFF9C27B0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task2Screen() {
    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {
                val colorName = text.lowercase().trim()

                if (colorPalette.containsKey(colorName)) {

                    buttonColor = colorPalette[colorName]!!
                    Log.d("Task2_Color", "Цвет '$colorName' найден, применён: $buttonColor")
                } else {

                    Log.e("Task2_Color", "Цвет '$colorName' НЕ найден в палитре!")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Применить цвет", color = Color.White)
        }


        Text("Доступные цвета:", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorPalette.toList()) { (name, color) ->
                Button(
                    onClick = {
                        text = name
                        buttonColor = color
                        Log.d("Task2_Palette", "Выбран цвет из палитры: $name")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = name.replaceFirstChar { it.uppercase() },
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Task2ScreenPreview() {
    MaterialTheme {
        Task2Screen()
    }
}