@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class SecondInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar =
                    {
                        TopAppBar(
                            title = { Text("Расчёт вкладов") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary, // фон
                                titleContentColor = MaterialTheme.colorScheme.onPrimary, // цвет заголовка
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }) { innerPadding ->
                    RateSelectionScreen(innerPadding)
                }

                }
            }
        }
    @Composable
    fun RateSelectionScreen(innerPadding: PaddingValues){
        // Состояние срока (в виде строки, чтобы обрабатывать ввод)
        var termInput by remember { mutableStateOf("") }
        // Выбранная ставка
        var selectedRate by remember { mutableStateOf<Double?>(null) }
        // Сообщение об ошибке
        var errorMessage by remember { mutableStateOf<String?>(null) }
        // Развёрнут ли выпадающий список
        var expanded by remember { mutableStateOf(false) }

        // Вспомогательная функция для парсинга срока
        fun parseTerm(): Int? = termInput.toIntOrNull()

        // Логика определения доступных ставок в зависимости от срока
        val availableRates = remember(termInput) {
            val term = parseTerm()
            when {
                term == null -> emptyList()       // срок не число
                term < 6 -> listOf(15.0)          // ставка 15%
                term in 6..11 -> listOf(10.0)     // ставка 10%
                term >= 12 -> listOf(5.0)         // ставка 5%
                else -> emptyList()
            }
        }

        // Проверка на наличие срока и валидность
        LaunchedEffect(termInput) {
            val term = parseTerm()
            errorMessage = when {
                termInput.isBlank() -> "Укажите срок (в месяцах)"
                term == null -> "Введите корректное число"
                else -> null
            }
            // Если срок некорректный или нет доступных ставок, сбрасываем выбранную ставку
            if ((errorMessage != null) || availableRates.isEmpty()) {
                selectedRate = null
            } else {
                // Автоматически выбираем первую доступную ставку, если ранее не было выбрано
                if (selectedRate !in availableRates) {
                    selectedRate = availableRates.firstOrNull()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        )
        {
            // Поле ввода срока
        OutlinedTextField(
            value = termInput,
            onValueChange = { termInput = it },
            label = { Text("Срок (месяцы)") },
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Выпадающий список выбора ставки
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedRate?.let { "$it%" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (errorMessage != null || availableRates.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Недоступно", color = MaterialTheme.colorScheme.error) },
                        onClick = { /* ничего не делаем */ },
                        enabled = false
                    )
                } else {
                    availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate%") },
                            onClick = {
                                selectedRate = rate
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Отображение выбранной ставки (для наглядности)
        if (selectedRate != null && errorMessage == null) {
            Text(
                text = "Выбрана ставка: ${selectedRate}%",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        }
    }
}

