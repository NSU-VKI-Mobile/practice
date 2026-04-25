package ci.nsu.mobile.main.ui.history

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.ui.base.BaseScreen
import ci.nsu.mobile.main.ui.base.ButtonPanel
import ci.nsu.mobile.main.ui.base.DepositView
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel
import ci.nsu.mobile.main.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    val deposits by viewModel.deposits.collectAsState()

    BaseScreen(title = "Результат расчета") {
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(deposits) { item ->
                DepositView(item)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        ButtonPanel {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.clear()
                    Toast.makeText(context, "Отчищено", Toast.LENGTH_SHORT).show()
                }) {
                Text("Отчистить")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onBack()
                }) {
                Text("На главную")
            }
        }
    }
}