@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.Components.AnimatedButton
import ci.nsu.mobile.main.ViewModels.MainInputViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainInputActivity : ComponentActivity() {
    private lateinit var viewModel: MainInputViewModel

    // Регистрируем обработчик результата
    private val getResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedTerm = result.data?.getIntExtra("UPDATED_TERM", 0) ?: 0
            if (updatedTerm > 0) {
                viewModel.updateTermMonths(updatedTerm.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализируем ViewModel (фабрика с SavedStateHandle)
        viewModel = ViewModelProvider(
            this, SavedStateViewModelFactory(application, this)
        )[MainInputViewModel::class.java]

        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            title = { Text("Расчёт вкладов") }, navigationIcon = {
                            IconButton(onClick = { finish() }) {
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
                    // Наблюдаем за состояниями из ViewModel
                    val startAmount by viewModel.startAmount.collectAsState()
                    val termMonths by viewModel.termMonths.collectAsState()

                    MainInputActivityScreen(
                        startAmount = startAmount,
                        onStartAmountChange = { viewModel.updateStartAmount(it) },
                        termMonths = termMonths,
                        onTermMonthsChange = { viewModel.updateTermMonths(it) },
                        onNextClick = {
                            val startAmountValue = viewModel.getStartAmountValue()
                            val termMonthsValue = viewModel.getTermMonthsValue()
                            val intent = Intent(this, SecondInputActivity::class.java).apply {
                                putExtra("START_AMOUNT", startAmountValue)
                                putExtra("TERM", termMonthsValue)
                            }
                            getResultLauncher.launch(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun MainInputActivityScreen(
        startAmount: String,
        onStartAmountChange: (String) -> Unit,
        termMonths: String,
        onTermMonthsChange: (String) -> Unit,
        onNextClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        // Проверяем, что оба поля не пустые
        val isFormValid = startAmount.isNotBlank() && termMonths.isNotBlank()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = startAmount,
                onValueChange = onStartAmountChange,
                label = { Text("Стартовый взнос") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = termMonths,
                onValueChange = onTermMonthsChange,
                label = { Text("Срок вклада (месяцы)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedButton(onNextClick, isFormValid, "Далее")
        }
    }
}