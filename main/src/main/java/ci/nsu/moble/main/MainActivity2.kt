package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme
import kotlin.random.Random

class MainActivity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("APP_LOG", "Application started")

        setContent {
            SimpleColorApp()
        }
    }
}

@Composable
fun SimpleColorApp() {

    var text by remember { mutableStateOf("") }

    var backgroundColor by remember { mutableStateOf(Color.White) }

    var buttonColor by remember { mutableStateOf(Color.Gray) }

    PracticeTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Введите цвет") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {


                        backgroundColor = Color(
                            Random.nextFloat(),
                            Random.nextFloat(),
                            Random.nextFloat()
                        )


                        buttonColor = when (text.lowercase()) {

                            "red" -> Color.Red
                            "green" -> Color.Green
                            "blue" -> Color.Blue
                            "black" -> Color.Black
                            "magenta" -> Color.Magenta

                            else -> Color.Gray
                        }

                        Log.d("APP_LOG", "User typed: $text")

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Нажми меня")
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text("Палитра цветов:")

                Spacer(modifier = Modifier.height(10.dp))

                Row {

                    ColorButton(Color.Red)
                    ColorButton(Color.Green)
                    ColorButton(Color.Blue)
                    ColorButton(Color.Magenta)

                }
            }
        }
    }
}

@Composable
fun ColorButton(color: Color) {

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color)
            .padding(4.dp)
    )
}