package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.DraftTheme

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent { //Устанавливает Compose UI
                DraftTheme { //Оборачивает в тему (цвета)
                    Surface( //Контейнер с фоном
                        modifier = Modifier.fillMaxSize(), //Растягивает на весь экран
                        color = MaterialTheme.colorScheme.background //Устанавливает цвет фона из темы
                    ) {
                        TemperatureScreen()
                    }
                }
            }
        }
    }
@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    //by - автоматически обновляет переменную
    //uiState - при изменении — экран перерисовывается

    Column( //Вертикальный контейнер для элементов.
        modifier = Modifier
            .fillMaxSize() //на весь экран
            .padding(24.dp), //отступы от краев
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Конвертер температуры",
            style = MaterialTheme.typography.headlineMedium, //headlineMedium — крупный заголовок
            color = MaterialTheme.colorScheme.primary //primary — основной цвет темы
        )

        OutlinedTextField( //Поле ввода для Цельсия
            value = uiState.celsius, //Текст, который показывается в поле
            onValueChange = { viewModel.onCelsiusChanged(it) }, //Вызывается при каждом изменении текста
            label = { Text("Цельсий (°C)") },
            placeholder = { Text("Введите температуру") },
            isError = uiState.celsius.isNotBlank() && !uiState.isCelsiusValid,
            supportingText = {
                if (uiState.celsius.isNotBlank() && !uiState.isCelsiusValid) {
                    Text("Введите корректное число")
                }
            },
            singleLine = true, //Одна строка (без переноса)
            modifier = Modifier.fillMaxWidth() //на всю ширину
        )

        Icon( //Иконка стрелки между полями
            imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
            contentDescription = "Конвертировать",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )

        OutlinedTextField( // Поле для Фаренгейта
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Фаренгейт (°F)") },
            placeholder = { Text("Введите температуру") },
            isError = uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid,
            supportingText = {
                if (uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid) {
                    Text("Введите корректное число")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.clearFields() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(
                text = "Очистить всё",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Card( //Карточка с формулами конвертации
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant //слегка изменённый цвет фона
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Формулы конвертации:",
                    style = MaterialTheme.typography.titleSmall
                )
                Text("°F = (°C × 9/5) + 32")
                Text("°C = (°F - 32) × 5/9")
            }
        }
    }
}