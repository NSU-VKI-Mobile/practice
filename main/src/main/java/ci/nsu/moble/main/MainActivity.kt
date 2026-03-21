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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

data class ColorItem(
    val name: String,
    val color: Color
)

val availableColors = listOf(
    ColorItem("red", Color.Red),
    ColorItem("green", Color.Green),
    ColorItem("blue", Color.Blue),
    ColorItem("yellow", Color.Yellow),
    ColorItem("orange", Color(0xFFFF9800)),
    ColorItem("purple", Color(0xFF9C27B0)),
    ColorItem("pink", Color(0xFFFFC0CB)),
    ColorItem("gray", Color.Gray),
    ColorItem("black", Color.Black),
    ColorItem("white", Color.White)
)

@Composable
fun ColorSearchScreen() {
    var searchText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFF6200EE)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Введите цвет (red, green, blue...)") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val foundColor = availableColors.find {
                    it.name.equals(searchText.trim(), ignoreCase = true)
                }

                if (foundColor != null) {
                    buttonColor = foundColor.color

                } else {

                    buttonColor = Color(0xFF6200EE)
                    Log.w("ColorSearch", "Пользовательский цвет '${searchText}' не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Применить цвет",
                color = if (buttonColor == Color(0xFF6200EE)) Color.Black else Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Доступные цвета:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableColors) { colorItem ->
                ColorPaletteItem(
                    colorName = colorItem.name,
                    colorValue = colorItem.color
                )
            }
        }
    }
}

@Composable
fun ColorPaletteItem(colorName: String, colorValue: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colorValue, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = colorName.replaceFirstChar { it.uppercase() },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (colorValue == Color.White || colorValue == Color.Yellow) Color.Black else Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}