// Task_5: Этап 1 — стартовый взнос + срок вклада.

package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Stage1State

@Composable
fun Stage1Screen(viewModel: MainViewModel) {
    val state by viewModel.stage1.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Этап 1: Основные параметры", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = viewModel::onInitialAmountChanged,
            label = { Text("Стартовый взнос") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.periodMonths,
            onValueChange = viewModel::onPeriodMonthsChanged,
            label = { Text("Срок вклада (мес.)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        val error = state.error
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = androidx.compose.ui.graphics.Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.navigateTo(ci.nsu.moble.main.ui.Screen.Main) },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
            Button(
                onClick = viewModel::onStage1Next,
                modifier = Modifier.weight(1f)
            ) {
                Text("Далее")
            }
        }
    }
}
