package com.example.colorlab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val colorMap = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
        "gray" to Color.Gray
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var inputText by remember { mutableStateOf("") }
            var buttonColor by remember { mutableStateOf(Color.LightGray) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Введите цвет (red, blue...)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val color = colorMap[inputText.lowercase()]
                        if (color != null) {
                            buttonColor = color
                        } else {
                            Log.d("COLOR_SEARCH", "Цвет \"$inputText\" не найден")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(buttonColor)
                ) {
                    Text("Найти цвет")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Доступные цвета:")

                LazyColumn {
                    items(colorMap.keys.toList()) { name ->
                        Text(
                            text = name,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}