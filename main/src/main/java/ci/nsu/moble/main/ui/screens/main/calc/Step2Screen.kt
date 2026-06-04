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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.viewmodel.CalcScreensViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(vm: CalcScreensViewModel, onBack: () -> Unit, goToResult: () -> Unit, bottomPadding: Dp) {
    val rate = vm.getRate()
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Новый расчёт (2/2)")}, windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })

        },
        modifier = Modifier.padding(bottom = bottomPadding)
    )
    { innerPadding ->
        val state by vm.uiState.collectAsStateWithLifecycle()
        Box (Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Доп. параметры", Modifier.align(Alignment.CenterHorizontally),style = MaterialTheme.typography.headlineSmall)

                Card() {
                    Column(Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            OutlinedTextField(
                                value = if(state.duration.isEmpty()) "Срок не указан!" else "$rate%",
                                onValueChange = {},
                                label = { Text("Процентная ставка") },
                                readOnly = true,
                                colors = TextFieldDefaults.colors(disabledTextColor = Color.Red)
                            )

                            OutlinedTextField(state.monthlyAdd, { vm.updateMonthlyAdd(it) }, label = { Text("Ежемесячное пополнение") }, keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                            )
                        }

                        Button(onClick = goToResult, Modifier.fillMaxWidth()) { Text("Рассчитать") }
                    }
                }

            }
        }
    }

}