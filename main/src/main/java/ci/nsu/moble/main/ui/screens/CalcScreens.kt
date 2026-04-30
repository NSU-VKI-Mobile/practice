package ci.nsu.moble.main.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.viewmodel.DepositViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(nav: NavController, vm: DepositViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Этап 1")},
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
        },
        bottomBar = {
            BottomAppBar(windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)) {
                Button(onClick = { nav.popBackStack() }, Modifier.fillMaxWidth().padding(horizontal = 16.dp)) { Text("В начало") }
            }
        }
    )
    { innerPadding ->
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
                                vm.amount,
                                { if (it.all { char -> char.isDigit() || char == '.' }) vm.amount = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Стартовый взнос") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                vm.duration,
                                { if (it.all { char -> char.isDigit() || char == '.' }) vm.duration = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Срок (мес)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Button(
                            onClick = { nav.navigate("step2") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = vm.amount.isNotEmpty() && vm.duration.isNotEmpty()
                        ) { Text("Далее") }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(nav: NavController, vm: DepositViewModel) {
    val rate = vm.getRate()
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Этап 2")}, windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
        },
        bottomBar = {
            BottomAppBar(windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)) {
                Button(onClick = { nav.popBackStack() }, Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("Назад")
                }
            }
        }

    )
    { innerPadding ->
        Box (Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Доп. параметры", Modifier.align(Alignment.CenterHorizontally),style = MaterialTheme.typography.headlineSmall)

                Card() {
                    Column(Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            OutlinedTextField(
                                value = if(vm.duration.isEmpty()) "Срок не указан!" else "$rate%",
                                onValueChange = {},
                                label = { Text("Процентная ставка") },
                                readOnly = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(disabledTextColor = Color.Red)
                            )

                            OutlinedTextField(vm.monthlyAdd, { vm.monthlyAdd = it }, label = { Text("Ежемесячное пополнение") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        }

                        Button(onClick = { nav.navigate("result") }, Modifier.fillMaxWidth()) { Text("Рассчитать") }
                    }
                }

            }
        }
    }

}

@Composable
fun ResultScreen(nav: NavController, vm: DepositViewModel) {
    val result = remember { vm.calculate() }
    Box (Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Card(Modifier.padding(16.dp).fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Итог: ${"%.2f".format(result.finalAmount)} руб.")
                Text("Прибыль: ${"%.2f".format(result.profit)} руб.")
                Text("Ставка: ${result.rate}%")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { vm.save(result); nav.navigate("main") }) { Text("Сохранить и выйти") }
                TextButton(onClick = { nav.navigate("main") }) { Text("В начало") }
            }
        }
    }
}