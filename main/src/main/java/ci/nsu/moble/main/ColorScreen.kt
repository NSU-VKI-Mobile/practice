package ci.nsu.moble.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ColorItem(
    val name: String,
    val color: Color
)

@Composable
fun ColorScreen() {
    val palette = listOf(
        ColorItem("r", Color(0xFFFF0000)),
        ColorItem("o", Color(0xFFFFA500)),
        ColorItem("l", Color(0xFFFFFF00)),
        ColorItem("e", Color(0xFF00FF00)),
        ColorItem("x", Color(0xFF0000FF)),
        ColorItem("chanel", Color(0xFF4B0082)),
        ColorItem("chanell", Color(0xFF8A2BE2))
    )

    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFFE6DDF0)) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Введите цвет") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val input = text.trim().lowercase()
                    val foundColor = palette.find { it.name == input }?.color

                    if (foundColor != null) {
                        buttonColor = foundColor
                    } else {
                        Log.d(
                            "CVEYETEST",
                            "Пользовательский цвет \"$text\" не найден.Это так печально"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text("Применить цвет")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(palette) { item ->
                    ColorCard(item)
                }
            }
        }
    }
}

@Composable
fun ColorCard(item: ColorItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = item.color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}