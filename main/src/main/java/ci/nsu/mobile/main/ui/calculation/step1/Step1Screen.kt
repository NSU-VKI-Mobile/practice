package ci.nsu.mobile.main.ui.calculation.step1

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.base.BaseScreen
import ci.nsu.mobile.main.ui.base.ButtonPanel
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    viewModel: CalculationViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    BaseScreen(title = "Первый этап расчета") {
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.initialAmount,
            onValueChange = { viewModel.initialAmount = it },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.months,
            onValueChange = { viewModel.months = it },
            label = { Text("Срок вклада (в месяцах)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )


        Spacer(modifier = Modifier.weight(1f))

        ButtonPanel {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onBack()
                }) {
                Text("Назад")
            }
            Button(
                modifier = Modifier.weight(1f),
                enabled = viewModel.initialAmount.isNotBlank() && viewModel.months.isNotBlank(),
                onClick = {
                    val error = viewModel.validateStep1()
                    if (error == null) onNext()
                    else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("Далее")
            }
        }

    }


}