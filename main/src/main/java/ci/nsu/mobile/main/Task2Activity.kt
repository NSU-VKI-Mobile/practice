package ci.nsu.mobile.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class Task2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Task2Screen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Task2Screen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    val buttonColors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )

    val colorNames = listOf(
        "Красный",
        "Зелёный",
        "Синий",
        "Жёлтый",
        "Пурпурный",
        "Голубой"
    )

    var currentButtonColor by remember { mutableStateOf(Color.Gray) }
    var visibleColors by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("TASK2", "Кнопка нажата")
                Log.d("TASK2", "Текст из поля: $inputText")

                currentButtonColor = buttonColors.random()
                visibleColors = colorNames.shuffled().take(3)

                Log.d("TASK2", "Цвет кнопки изменён")
                Log.d("TASK2", "Показаны цвета: $visibleColors")
            },
            colors = ButtonDefaults.buttonColors(containerColor = currentButtonColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Нажми меня")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Список цветов:")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(visibleColors) { colorName ->
                Text(text = colorName)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}