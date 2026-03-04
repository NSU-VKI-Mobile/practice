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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.*

// Словарь только с английскими названиями (ключи с заглавной буквы)
private val colorsMap = mapOf(
    "Red" to Red,
    "Orange" to Orange,
    "Yellow" to Yellow,
    "Green" to Green,
    "Blue" to Blue,
    "Indigo" to Indigo,
    "Violet" to Violet,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Main() {
    var input by remember { mutableStateOf("") }
    val buttonColor = remember { mutableStateOf(Color.Transparent) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введите цвет (англ.)") },
            placeholder = { Text("Текст") },
            singleLine = true
        )

        Button(
            onClick = {
                val trimmed = input.trim()

                val formatted = trimmed.lowercase()
                    .replaceFirstChar { it.uppercase() }
                val color = colorsMap[formatted]

                if (color != null) {
                    buttonColor.value = color
                    Log.d("ColorSearch", "Цвет \"$trimmed\" найден")
                } else {
                    Log.d("ColorSearch", "Цвет \"$trimmed\" не найден")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonColor.value == Color.Transparent)
                    MaterialTheme.colorScheme.primary
                else
                    buttonColor.value
            )
        ) {
            Text("Применить цвет")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colorsMap.toList()) { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        color = Color.Black // Для контраста на цветном фоне
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color)
                    )
                }
            }
        }
    }
}