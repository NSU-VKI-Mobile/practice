package ci.nsu.mobile.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val colorsMap = mapOf(
    "Red"    to Color.Red,
    "Orange" to Color(0xFFFF6600),
    "Yellow" to Color.Yellow,
    "Green"  to Color.Green,
    "Blue"   to Color.Blue,
    "Indigo" to Color(0xFF4B0082),
    "Violet" to Color(0xFF8B00FF)
)

@Composable
fun ColorScreen() {
    var inputText by remember { mutableStateOf("") }

    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val found = colorsMap.entries.find {
                    it.key.equals(inputText.trim(), ignoreCase = true)
                }

                if (found != null) {
                    buttonColor = found.value
                } else {
                    Log.d("ColorSearch", "123 цвет не найден: ${inputText.trim()}")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить цвет", color = Color.White)
        }

        Text("Палитра доступных цветов:", fontSize = 16.sp)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(colorsMap.entries.toList()) { entry ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(entry.value, RoundedCornerShape(12.dp))
                        .padding(start = 16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.CenterStart
                ) {
                    Text(entry.key, color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}