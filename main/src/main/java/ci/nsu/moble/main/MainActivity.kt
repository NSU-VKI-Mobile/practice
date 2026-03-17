package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

private val colorsMap = mapOf(
    "red" to Color.Red,
    "orange" to Color(0xFFFFA500),
    "yellow" to Color.Yellow,
    "green" to Color.Green,
    "blue" to Color.Blue,
    "indigo" to Color(0xFF4B0082),
    "violet" to Color(0xFF8A2BE2)
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorSearchScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSearchScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFFB39DDB)) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val normalizedColor = inputText.trim().lowercase()
                val foundColor = colorsMap[normalizedColor]

                if (foundColor != null) {
                    buttonColor = foundColor
                } else {
                    Log.d(
                        "COLOR_SEARCH",
                        "Пользовательский цвет \"$inputText\" не найден"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Text("Применить цвет")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(colorsMap.keys.toList()) { colorName ->
                PaletteItem(colorName = colorName)
            }
        }

    }
}

@Composable
fun PaletteItem(colorName: String) {
    val colorValue = colorsMap[colorName.lowercase()] ?: Color.LightGray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = colorValue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = colorName,
            modifier = Modifier.padding(top = 16.dp),
            color = if (colorName.lowercase() == "yellow") Color.Black else Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}