package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ColorSearchScreen()
            }
        }
    }
}

private const val TAG = "ColorSearch"

@Composable
fun ColorSearchScreen() {
    val colorsMap = remember {
        linkedMapOf(
            "Red" to Color.Red,
            "Orange" to Color(0xFFFFA500),
            "Yellow" to Color.Yellow,
            "Green" to Color.Green,
            "Blue" to Color.Blue,
            "Indigo" to Color(0xFF4B0082),
            "Violet" to Color(0xFF8F00FF),
            "Black" to Color.Black,
            "White" to Color.White,
            "Gray" to Color.Gray
        )
    }

    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.LightGray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val foundColor = colorsMap.entries.firstOrNull {
                    it.key.equals(inputText.trim(), ignoreCase = true)
                }?.value

                if (foundColor != null) {
                    buttonColor = foundColor
                    Log.d(TAG, "Цвет \"$inputText\" найден")
                } else {
                    Log.d(TAG, "Пользовательский цвет \"$inputText\" не найден")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(14.dp),
            colors = CardDefaults.cardColors().let {
                androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            }
        ) {
            Text("Применить цвет")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Палитра цветов",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorsMap.entries.toList()) { entry ->
                ColorItem(name = entry.key, color = entry.value)
            }
        }
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            color = if (
                color == Color.Black ||
                color == Color.Blue ||
                color == Color(0xFF4B0082)
            ) Color.White else Color.Black
        )
    }
}