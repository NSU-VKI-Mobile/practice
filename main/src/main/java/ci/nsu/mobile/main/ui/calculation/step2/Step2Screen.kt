package ci.nsu.mobile.main.ui.calculation.step2

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.base.BaseScreen
import ci.nsu.mobile.main.ui.base.ButtonPanel
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    viewModel: CalculationViewModel,
    onResult: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    val rate = viewModel.getAvailableRate()



    BaseScreen(title = "Второй этап расчета") {
        Spacer(modifier = Modifier.height(24.dp))

        if (rate == null) {
            Text("Введите корректный срок вклада")
        } else {
            Text("Доступная ставка: $rate%")
            viewModel.rate = rate
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (₽)") },
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
                onClick = {
                    val error = viewModel.validateStep2()
                    if (error == null) onResult()
                    else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("Расчитать")
            }
        }
    }
}