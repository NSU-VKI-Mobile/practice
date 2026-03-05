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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.PracticeTheme

// Структура данных для хранения цветов
data class ColorPalette(
    val name: String,
    val color: Color
)

// Галерея доступных цветов
val colorGallery = listOf(
    ColorPalette("Red", Color.Red),
    ColorPalette("Orange", Color(0xFFFFA500)),
    ColorPalette("Yellow", Color.Yellow),
    ColorPalette("Green", Color.Green),
    ColorPalette("Blue", Color.Blue),
    ColorPalette("Violet", Color(0xFF4B0082)),
    ColorPalette("Pink", Color(0xFFEE82EE))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ColorFinderScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorFinderScreen() {
    var searchInput by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    // Функция поиска цвета
    fun findColorByName(input: String): Color? {
        val normalizedInput = input.trim().lowercase()
        val matchedColor = colorGallery.find {
            it.name.lowercase() == normalizedInput
        }

        if (matchedColor == null) {
            Log.d("ColorFinder", "Цвет '$input' не найден в коллекции")
            return null
        }

        return matchedColor.color
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода
        OutlinedTextField(
            value = searchInput,
            onValueChange = { searchInput = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка поиска цвета
        Button(
            onClick = {
                selectedColor = findColorByName(searchInput)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedColor ?: Color(0xFF1565C0)
            )
        ) {
            Text(
                text = "Найти цвет",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White

            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Заголовок для коллекции
        Text(
            text = "Доступные цвета:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Список цветов с текстом на цветном фоне
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorGallery) { paletteItem ->
                ColorChipItem(paletteItem)
            }
        }
    }
}

@Composable
fun ColorChipItem(paletteItem: ColorPalette) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = paletteItem.color,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = paletteItem.name,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ColorFinderScreenPreview() {
    PracticeTheme {
        ColorFinderScreen()
    }
}