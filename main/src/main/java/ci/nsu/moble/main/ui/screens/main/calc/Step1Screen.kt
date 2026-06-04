package ci.nsu.moble.main.ui.screens.main.calc
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.viewmodel.CalcScreensViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(vm: CalcScreensViewModel, goToStep2: () -> Unit, bottomPadding: Dp) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Новый расчёт (1/2)")},
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
        },
        modifier = Modifier.padding(bottom = bottomPadding)
    )
    { innerPadding ->
        val state by vm.uiState.collectAsStateWithLifecycle()
        Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(
                Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Основные данные", modifier = Modifier.align(Alignment.CenterHorizontally) , style = MaterialTheme.typography.headlineSmall)

                Card() {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            OutlinedTextField(
                                state.amount,
                                { if (it.all { char -> char.isDigit() || char == '.' }) vm.updateAmount(it) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Стартовый взнос") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                state.duration,
                                { if (it.all { char -> char.isDigit() || char == '.' }) vm.updateDuration(it) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Срок (мес)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Button(
                            onClick = goToStep2,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = state.amount.isNotEmpty() && state.duration.isNotEmpty()
                        ) { Text("Далее") }

                    }
                }
            }
        }
    }
}