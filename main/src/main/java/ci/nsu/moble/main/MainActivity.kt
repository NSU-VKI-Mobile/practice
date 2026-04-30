package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorPickerScreen()
                }
            }
        }
    }
}

@Composable
fun ColorPickerScreen(
    viewModel: ColorPickerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок
        Text(
            text = "Выбор цвета",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Предпросмотр цвета в карточке
        Card(
            modifier = Modifier.size(200.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(uiState.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Предпросмотр",
                    color = if (uiState.red + uiState.green + uiState.blue > 380)
                        Color.Black else Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // HEX код цвета
        Card(
            modifier = Modifier.padding(horizontal = 32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = uiState.hexCode,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Слайдер для красного
        ColorSlider(
            label = "Красный",
            value = uiState.red,
            onValueChange = viewModel::onRedChanged,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Слайдер для зеленого
        ColorSlider(
            label = "Зеленый",
            value = uiState.green,
            onValueChange = viewModel::onGreenChanged,
            color = Color.Green
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Слайдер для синего
        ColorSlider(
            label = "Синий",
            value = uiState.blue,
            onValueChange = viewModel::onBlueChanged,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка случайного цвета
        Button(
            onClick = { viewModel.generateRandomColor() },
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Случайный цвет",
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Текущие значения RGB
        Text(
            text = "RGB: (${uiState.red}, ${uiState.green}, ${uiState.blue})",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ColorSlider(
    label: String,
    value: Int,
    onValueChange: (Float) -> Unit,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label: $value",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Slider(
            value = value.toFloat(),
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            steps = 254,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}