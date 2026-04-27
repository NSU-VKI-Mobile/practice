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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.Components.AnimatedButton
import ci.nsu.mobile.main.ViewModels.SecondInputViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class SecondInputActivity : ComponentActivity() {

    private lateinit var viewModel: SecondInputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Получаем параметры из Intent
        val defaultTerm = intent.getIntExtra("TERM", 0)
        val startAmount = intent.getDoubleExtra("START_AMOUNT", 0.0)

        // Создаём ViewModel с поддержкой SavedStateHandle
        viewModel = ViewModelProvider(
            this, SavedStateViewModelFactory(application, this)
        )[SecondInputViewModel::class.java]

        // Инициализируем ViewModel начальными данными (только при первом создании)
        viewModel.initializeFromIntent(startAmount, defaultTerm)

        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            title = { Text("Расчёт вкладов") }, navigationIcon = {
                            IconButton(onClick = {
                                val resultIntent = Intent().apply {
                                    putExtra("UPDATED_TERM", viewModel.getTermInt())
                                    putExtra("UPDATED_RATE", viewModel.getRateDouble())
                                }
                                setResult(RESULT_OK, resultIntent)
                                finish()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Назад"
                                )
                            }
                        }, colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ), modifier = Modifier.fillMaxWidth()
                        )
                    }) { innerPadding ->
                    // Подписываемся на состояния из ViewModel
                    val term by viewModel.term.collectAsState()
                    val rate by viewModel.rate.collectAsState()
                    val currency by viewModel.currency.collectAsState()

                    RateSelectionScreen(
                        innerPadding = innerPadding,
                        termInput = term,
                        onTermInputChange = { viewModel.updateTerm(it) },
                        selectedRate = rate,
                        onSelectedRateChange = { viewModel.updateRate(it) },
                        selectedCurrency = currency,
                        onCurrencyChange = { viewModel.updateCurrency(it) })
                }
            }
        }
    }

    @Composable
    fun RateSelectionScreen(
        innerPadding: PaddingValues,
        termInput: String,
        onTermInputChange: (String) -> Unit,
        selectedRate: Double?,
        onSelectedRateChange: (Double?) -> Unit,
        selectedCurrency: String,                // текущая валюта
        onCurrencyChange: (String) -> Unit       // колбэк изменения валюты
    ) {
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var expanded by remember { mutableStateOf(false) }
        var currencyExpanded by remember { mutableStateOf(false) }  // состояние для выпадающего списка валют

        val allRates = listOf(15.0, 10.0, 5.0)
        val currencies = listOf("Рубли (RUB)", "Доллары (USD)", "Евро (EUR)")

        val context = LocalContext.current

        fun parseTerm(): Int? = termInput.toIntOrNull()

        val isFormValid = termInput.isNotBlank()

        fun getTermForRate(rate: Double): Int = when (rate) {
            15.0 -> 5
            10.0 -> 10
            5.0 -> 12
            else -> 0
        }

        fun getConditionForRate(rate: Double): String = when (rate) {
            15.0 -> "для срока < 6 месяцев"
            10.0 -> "для срока от 6 до 11 месяцев"
            5.0 -> "для срока ≥ 12 месяцев"
            else -> ""
        }

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
                if (selectedRate != recommendedRate) {
                    onSelectedRateChange(recommendedRate)
                }
            } else {
                onSelectedRateChange(null)
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
                onValueChange = onTermInputChange,
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

            // Выпадающий список процентной ставки
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
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    allRates.forEach { rate ->
                        DropdownMenuItem(text = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "$rate%", style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = getConditionForRate(rate),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }, onClick = {
                            onSelectedRateChange(rate)
                            onTermInputChange(getTermForRate(rate).toString())
                            expanded = false
                        })
                    }
                }
            }


            // Выпадающий список валют
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
                    expanded = currencyExpanded, onDismissRequest = { currencyExpanded = false }) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(text = { Text(currency) }, onClick = {
                            onCurrencyChange(currency)
                            currencyExpanded = false
                        })
                    }
                }
            }

            // Отображение выбранной ставки
            if (selectedRate != null && errorMessage == null) {
                Text(
                    text = "Выбрана ставка: ${selectedRate}%",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedButton({
                val intent = Intent(context, ResultActivity::class.java).apply {
                    putExtra("START_AMOUNT", viewModel.startAmount.value)
                    putExtra("TERM", viewModel.getTermInt())
                    putExtra("RATE", viewModel.getRateDouble())
                    putExtra("CURRENCY", viewModel.currency.value)
                }
                context.startActivity(intent)
            }, isFormValid, "Рассчитать", Modifier.fillMaxWidth())
        }
    }
}