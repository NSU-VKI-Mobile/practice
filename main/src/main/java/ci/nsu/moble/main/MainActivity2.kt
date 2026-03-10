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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.PracticeTheme

private const val TAG = "ColorSearch"

object ColorData {
    val colors = mapOf(
        "red" to Color.Red,
        "blue" to Color.Blue,
        "green" to Color.Green,
        "yellow" to Color.Yellow,
        "orange" to Color(0xFFFF9800),
        "violet" to Color(0xFF9C27B0),
        "pink" to Color(0xFFFFC0CB),
    )

    // Для отображения в списке палитры
    val colorList = colors.map { ColorItem(it.key, it.value) }
}

data class ColorItem(
    val name: String,
    val color: Color
)

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
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
    var searchQuery by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFF6200EE)) } // Стандартный цвет кнопки
    var searchPerformed by remember { mutableStateOf(false) }
    var showNotFoundMessage by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Поиск цветов",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Поле ввода
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                showNotFoundMessage = false
            },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка поиска
        Button(
            onClick = {
                searchPerformed = true
                val result = searchColor(searchQuery)
                if (result.found) {
                    buttonColor = result.color!!
                    showNotFoundMessage = false
                } else {
                    buttonColor = Color(0xFF6200EE) // Возвращаем стандартный цвет
                    if (searchQuery.isNotBlank()) {
                        showNotFoundMessage = true
                        Log.e(TAG, "Пользовательский цвет \"${searchQuery.lowercase()}\" не найден")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Найти цвет",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Сообщение о том, что цвет не найден
        if (showNotFoundMessage) {
            Text(
                text = "Цвет \"${searchQuery.lowercase()}\" не найден",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Разделитель
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        // Заголовок палитры
        Text(
            text = "Палитра цветов",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Список палитры цветов
        ColorPaletteList(colors = ColorData.colorList)
    }
}

@Composable
fun ColorPaletteList(colors: List<ColorItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(colors) { colorItem ->
            ColorPaletteItem(colorItem)
        }
    }
}

@Composable
fun ColorPaletteItem(colorItem: ColorItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colorItem.color, RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = colorItem.name,
            color = if (isColorDark(colorItem.color)) Color.White else Color.Black,
            fontSize = 16.sp
        )
    }
}

// Вспомогательная функция для определения темный цвет или светлый
fun isColorDark(color: Color): Boolean {
    val red = color.red * 255
    val green = color.green * 255
    val blue = color.blue * 255
    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)
    return luminance < 128
}

// Функция поиска цвета
fun searchColor(query: String): SearchResult {
    val normalizedQuery = query.lowercase().trim()
    val foundColor = ColorData.colors[normalizedQuery]

    return if (foundColor != null) {
        Log.d(TAG, "Цвет \"$normalizedQuery\" найден")
        SearchResult(found = true, color = foundColor)
    } else {
        SearchResult(found = false, color = null)
    }
}

data class SearchResult(
    val found: Boolean,
    val color: Color?
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        ColorSearchScreen(
            modifier = Modifier
        )
    }
}