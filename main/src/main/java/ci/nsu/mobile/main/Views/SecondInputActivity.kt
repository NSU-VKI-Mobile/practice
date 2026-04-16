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

    // Состояния, поднятые на уровень Activity
    private var termInputState by mutableStateOf("")
    private var selectedRateState by mutableStateOf<Double?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startAmount = intent.getDoubleExtra("START_AMOUNT", 0.0)
        val defaultTerm = intent.getIntExtra("TERM", 0)

        // Инициализируем состояние значением из Intent
        termInputState = if (defaultTerm > 0) defaultTerm.toString() else ""
        selectedRateState = null

        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Расчёт вкладов") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    // Теперь termInputState и selectedRateState доступны
                                    val resultIntent = Intent().apply {
                                        putExtra("UPDATED_TERM", termInputState.toIntOrNull() ?: 0)
                                        putExtra("UPDATED_RATE", selectedRateState ?: 0.0)
                                    }
                                    setResult(RESULT_OK, resultIntent)
                                    finish()
                                }) {
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
                        termInput = termInputState,
                        onTermInputChange = { termInputState = it },
                        selectedRate = selectedRateState,
                        onSelectedRateChange = { selectedRateState = it }
                    )
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
        onSelectedRateChange: (Double?) -> Unit
    ) {
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var expanded by remember { mutableStateOf(false) }

        val allRates = listOf(15.0, 10.0, 5.0)

        fun parseTerm(): Int? = termInput.toIntOrNull()

        fun getTermForRate(rate: Double): Int = when (rate) {
            15.0 -> 5
            10.0 -> 10
            5.0 -> 12
            else -> 0
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
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate%") },
                            onClick = {
                                onSelectedRateChange(rate)
                                onTermInputChange(getTermForRate(rate).toString())
                                expanded = false
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
        }
    }
}