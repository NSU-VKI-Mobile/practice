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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

// Структура данных для хранения цветов
data class ColorItem(
    val name: String,
    val color: Color
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
    // Доступные цвета
    val availableColors = remember {
        listOf(
            ColorItem("Red", Color.Red),
            ColorItem("Orange", Color(0xFFFFA500)),
            ColorItem("Yellow", Color.Yellow),
            ColorItem("Зеленый", Color.Green),
            ColorItem("Голубой", Color.Cyan),
            ColorItem("Синий", Color.Blue),
            ColorItem("Фиолетовый", Color.Magenta))

    }

    var searchText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Поле ввода
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Введите название цвета") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка поиска
            Button(
                onClick = {
                    val foundColor = availableColors.find {
                        it.name.equals(searchText, ignoreCase = true)
                    }

                    if (foundColor != null) {
                        buttonColor = foundColor.color
                    } else {
                        Log.d("ColorSearch", "Пользовательский цвет '$searchText' не найден")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Найти цвет",
                    color = if (buttonColor != null && buttonColor != Color.Black)
                        Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Заголовок для палитры
            Text(
                text = "Палитра цветов:",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Список цветов
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableColors) { colorItem ->
                    ColorListItem(colorItem)
                }
            }
        }
    }
}

@Composable
fun ColorListItem(colorItem: ColorItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Цветной квадратик
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorItem.color, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Название цвета
            Text(
                text = colorItem.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}