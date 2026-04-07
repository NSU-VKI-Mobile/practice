@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.content.Intent
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class SecondInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Получаем переданные значения из Intent
        val startAmount = intent.getDoubleExtra("START_AMOUNT", 0.0)
        val termMonths = intent.getIntExtra("TERM", 0)

        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Расчёт вкладов") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                ) { innerPadding ->
                    RateSelectionScreen(
                        innerPadding = innerPadding,
                        defaultTerm = termMonths
                    )
                }
            }
        }
    }

    @Composable
    fun RateSelectionScreen(innerPadding: PaddingValues, defaultTerm: Int) {
        var termInput by remember { mutableStateOf(if (defaultTerm > 0) defaultTerm.toString() else "") }
        var selectedRate by remember { mutableStateOf<Double?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var expanded by remember { mutableStateOf(false) }

        val currencies = listOf("Рубли (RUB)", "Доллары (USD)", "Евро (EUR)")
        var selectedCurrency by remember { mutableStateOf(currencies[0]) }
        var currencyExpanded by remember { mutableStateOf(false) }

        val context = LocalContext.current

        // Все возможные ставки
        val allRates = listOf(15.0, 10.0, 5.0)

        // Функция, возвращающая "типовой" срок для выбранной ставки
        fun getTermForRate(rate: Double): Int = when (rate) {
            15.0 -> 5   // для 15% срок < 6 месяцев
            10.0 -> 10  // для 10% срок от 6 до 11 месяцев
            5.0  -> 12  // для 5% срок >= 12 месяцев
            else -> 0
        }

        fun parseTerm(): Int? = termInput.toIntOrNull()

        // При ручном изменении срока выбираем подходящую ставку
        LaunchedEffect(termInput) {
            val term = parseTerm()
            errorMessage = when {
                termInput.isBlank() -> "Укажите срок (в месяцах)"
                term == null -> "Введите корректное число"
                else -> null
            }
            if (errorMessage == null && term != null) {
                val recommendedRate = when {
                    term < 6 -> 15.0
                    term in 6..11 -> 10.0
                    term >= 12 -> 5.0
                    else -> null
                }
                selectedRate = recommendedRate
            } else {
                selectedRate = null
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
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

            // Выпадающий список выбора ставки (всегда показывает все ставки)
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
                    // Всегда показываем все три ставки
                    allRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate%") },
                            onClick = {
                                selectedRate = rate
                                // При выборе ставки меняем срок на соответствующий
                                termInput = getTermForRate(rate).toString()
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Выпадающий список для выбора валюты
            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Валюта") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                selectedCurrency = currency
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            if (selectedRate != null && errorMessage == null) {
                Text(
                    text = "Выбрана ставка: ${selectedRate}%",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(onClick = {
                val intent = Intent(context, ResultActivity::class.java)
                context.startActivity(intent)
            }) { Text("Рассчитать") }
        }
    }
}