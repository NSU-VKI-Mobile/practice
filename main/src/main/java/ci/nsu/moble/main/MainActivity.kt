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
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ColorSearchScreen()
            }
        }
    }
}

val colors = mapOf(
    "red" to Color.Red,
    "blue" to Color.Blue,
    "green" to Color.Green,
    "yellow" to Color.Yellow,
    "orange" to Color(0xFFFF9800),
    "purple" to Color(0xFF9C27B0),
    "pink" to Color(0xFFFF69B4)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSearchScreen() {
    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }

    fun searchColor(name: String) {
        val color = colors[name.lowercase()]
        if (color != null) {
            buttonColor = color
        } else {
            buttonColor = null
            Log.d("ColorSearch", "Цвет '$name' не найден")
        }
    }

Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Введите цвет") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = { searchColor(text) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
        )
    ) {
        Text("Найти цвет")
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text("Палитра цветов", fontSize = 20.sp)

    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colors.toList()) { (name, color) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color)
                    .padding(16.dp)
            ) {
                Text(
                    text = name,
                    color = Color.Black
                )
            }
        }
    }
}
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}