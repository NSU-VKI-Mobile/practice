package ci.nsu.moble.main.ui

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.data.ColorData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var inputText by remember { mutableStateOf("") }
    var buttonBackgroundColor by remember { mutableStateOf(Color.Green) }
    var searchPerformed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Поиск цвета",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = inputText,
            onValueChange = {
                inputText = it.lowercase()
                searchPerformed = false
            },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                searchPerformed = true
                val foundColor = ColorData.colors[inputText.lowercase()]

                if (foundColor != null) {
                    buttonBackgroundColor = foundColor
                    Log.d("ColorSearch", "Цвет '$inputText' найден и применен")
                } else {
                    buttonBackgroundColor = Color.Green
                    Log.w("ColorSearch", "Пользовательский цвет '$inputText' не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (searchPerformed) buttonBackgroundColor else Color.Gray
            )
        ) {
            Text(
                text = "Применить цвет",
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Text(
            text = "Палитра цветов:",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        ColorPaletteList()
    }
}

@Composable
fun ColorPaletteList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = ColorData.colorList,
            key = { it.first }
        ) { (colorName, colorValue) ->
            ColorPaletteItem(
                colorName = colorName,
                colorValue = colorValue
            )
        }
    }
}

@Composable
fun ColorPaletteItem(
    colorName: String,
    colorValue: Color
) {
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
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = colorValue.toHexString(),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}


fun Color.toHexString(): String {
    return String.format("#%06X", (0xFFFFFF and this.hashCode()))
}