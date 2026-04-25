package ci.nsu.mobile.main.ui.calculation.result

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.base.BaseScreen
import ci.nsu.mobile.main.ui.base.ButtonPanel
import ci.nsu.mobile.main.ui.base.DepositView
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: CalculationViewModel,
    onMain: () -> Unit,
) {
    val context = LocalContext.current

    val deposit = viewModel.calculateResult()

    BaseScreen(title = "Результат расчета") {
        Spacer(modifier = Modifier.height(24.dp))

        DepositView(deposit)

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))
        ButtonPanel {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.save(deposit)
                    Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
                    onMain()
                }) {
                Text("Сохранить")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onMain()
                }) {
                Text("На главную")
            }
        }
    }
}