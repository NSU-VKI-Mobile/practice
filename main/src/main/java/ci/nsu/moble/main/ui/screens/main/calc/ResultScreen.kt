package ci.nsu.moble.main.ui.screens.main.calc
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.viewmodel.CalcScreensViewModel

@Composable
fun ResultScreen(vm: CalcScreensViewModel, goToStart: () -> Unit, goToResults:() -> Unit) {
    val result = remember { vm.calculate() }
    Box (Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Card(Modifier.padding(16.dp).fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Итог: ${"%.2f".format(result.finalAmount)} руб.")
                Text("Прибыль: ${"%.2f".format(result.interestEarned)} руб.")
                Text("Ставка: ${result.interestRate}%")
                Spacer(Modifier.height(20.dp))
                Button(onClick = {
                    vm.save(result)
                    goToResults()}) {
                    Text("Сохранить")
                }
                TextButton(onClick = goToStart) {
                    Text("Удалить")
                }
            }
        }
    }
}