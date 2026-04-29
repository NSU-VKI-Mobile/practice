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
import ci.nsu.moble.main.ui.theme.*

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

@Composable
fun ColorPickerScreen() {
    val colorMap = mapOf(
        "purple80" to Purple80,
        "purplegrey80" to PurpleGrey80,
        "pink80" to Pink80,
        "purple40" to Purple40,
        "purplegrey40" to PurpleGrey40,
        "pink40" to Pink40,
        "red" to Red,
        "orange" to Orange,
        "yellow" to Yellow,
        "green" to Green,
        "blue" to Blue,
        "indigo" to Indigo,
        "violet" to Violet
    )

    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Введите название цвета") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val colorName = inputText.trim().lowercase()
                        val foundColor = colorMap[colorName]
                        if (foundColor != null) {
                            buttonColor = foundColor
                        } else {
                            Log.d("ColorPicker", "пользовательский цвет '$colorName' не найден")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Применить цвет")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Палитра цветов",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colorMap.keys.toList()) { colorName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(colorMap[colorName]!!)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = colorName.replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }
        }
    )
}