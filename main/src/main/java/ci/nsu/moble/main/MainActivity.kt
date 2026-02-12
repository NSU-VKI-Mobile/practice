package ci.nsu.moble.main

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ColorScreen()
            }
        }
    }
}

val colorMap = mapOf(
    "Red" to Color.Red,
    "Orange" to Color(0xFFFFA500),
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Indigo" to Color(0xFF4B0082),
    "Violet" to Color(0xFFEE82EE)
)

@Composable
fun ColorScreen() {
    var textInput by remember { mutableStateOf("") }

    var buttonColor by remember { mutableStateOf(Color.Green) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = textInput,
            onValueChange = { newText -> textInput = newText },
            label = { Text("Введите название цвета (напр. Red)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val foundColor = colorMap[textInput]

                if (foundColor != null) {
                    buttonColor = foundColor
                } else {
                    Log.d("ColorCheck", "Пользовательский цвет \"$textInput\" не найден")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Применить цвет", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorMap.toList()) { (name, color) ->
                ColorListItem(colorName = name, colorValue = color)
            }
        }
    }
}

@Composable
fun ColorListItem(colorName: String, colorValue: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorValue),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = colorName,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}