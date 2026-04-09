package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.presentation.viewmodel.MainParamsViewModel

@Composable
fun MainParamsScreen(
    onNavigateBack: () -> Unit,
    onNavigateNext: (Double, Int) -> Unit
) {
    val viewModel: MainParamsViewModel = viewModel()

    val amount by viewModel.amount.collectAsState()
    val months by viewModel.months.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ввод основных параметров",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = amount,
            onValueChange = { viewModel.updateAmount(it) },
            label = { Text("Стартовый взнос (₽)") },
            placeholder = { Text("Например: 100000") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = months,
            onValueChange = { viewModel.updateMonths(it) },
            label = { Text("Срок вклада (месяцев)") },
            placeholder = { Text("Например: 12") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(onClick = onNavigateBack) { Text("В начало") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    // Валидацию отдаём ViewModel
                    when (val result = viewModel.validateAndProceed()) {
                        is MainParamsViewModel.MainParamsResult.Success ->
                            onNavigateNext(result.amount, result.months)
                        is MainParamsViewModel.MainParamsResult.Error -> {}
                        // Ошибка уже обновлена в UI через StateFlow
                    }
                },
                enabled = amount.isNotBlank() && months.isNotBlank()
            ) {
                Text("Далее")
            }
        }
    }
}