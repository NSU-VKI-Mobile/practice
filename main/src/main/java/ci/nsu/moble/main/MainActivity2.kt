package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme   // ← ИМПОРТ ТВОЕЙ ТЕМЫ

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleColorApp()
        }
    }
}

@Composable
fun SimpleColorApp() {

    var text by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.White) }

    PracticeTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor   // ← ВАЖНО! Меняем цвет Surface
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Введите red или green") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        backgroundColor = when (text.trim().lowercase()) {
                            "red" -> Color.Red
                            "green" -> Color.Green
                            else -> Color.White
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Нажми меня")
                }
            }
        }
    }
}