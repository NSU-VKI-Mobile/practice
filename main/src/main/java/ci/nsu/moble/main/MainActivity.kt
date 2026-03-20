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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.PracticeTheme

// Структура данных для хранения цветов
data class ColorItem(
    val name: String,
    val color: Color
)

// Палитра доступных цветов
val availableColors = listOf(
    ColorItem("Red", Color.Red),
    ColorItem("Orange", Color(0xFFFFA500)),
    ColorItem("Yellow", Color.Yellow),
    ColorItem("Green", Color.Green),
    ColorItem("Blue", Color.Blue),
    ColorItem("Indigo", Color(0xFF4B0082)),
    ColorItem("Violet", Color(0xFFEE82EE))
)



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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }

    // Функция поиска цвета
    fun findColorByName(name: String): Color? {
        val normalizedName = name.trim().lowercase()
        val foundColor = availableColors.find {
            it.name.lowercase() == normalizedName
        }

        if (foundColor == null) {
            Log.d("ColorSearch", "Пользовательский цвет '$name' не найден")
            return null
        }

        return foundColor.color
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка поиска цвета (её фон меняется при найденном цвете)
        Button(
            onClick = {
                buttonColor = findColorByName(searchQuery)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .then(
                    if (buttonColor != null) {
                        Modifier.background(buttonColor!!, RoundedCornerShape(12.dp))
                    } else {
                        Modifier
                    }
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor ?: Color(0xFF2196F3) // Синий по умолчанию
            )
        ) {
            Text(
                text = "Применить цвет",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (buttonColor != null &&
                    (buttonColor == Color.Black ||
                            buttonColor == Color.Blue ||
                            buttonColor == Color(0xFF4B0082)))
                    Color.White
                else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Заголовок для палитры
        Text(
            text = "Палитра цветов:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Список палитры цветов (просто отображение цветов)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableColors) { colorItem ->
                ColorPaletteItem(colorItem)
            }
        }
    }
}

@Composable
fun ColorPaletteItem(colorItem: ColorItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Цветной квадрат
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colorItem.color, RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Название цвета
        Text(
            text = colorItem.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
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