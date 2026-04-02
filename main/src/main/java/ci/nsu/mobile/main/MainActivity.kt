package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.repository.DepositRepository
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PracticeTheme {
                TestViewModelScreen()
            }
        }
    }
}

@Composable
fun TestViewModelScreen() {
    val context = LocalContext.current


    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { DepositRepository(db) }
    val factory = remember { DepositViewModelFactory(repository) }
    val viewModel: DepositViewModel = viewModel(factory = factory)  // ← viewModel() доступен

    val firstStepState by viewModel.firstStepState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✅ Repository и ViewModel работают!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Состояние первого этапа:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Стартовый взнос: ${firstStepState.initialAmount.ifEmpty { "не введён" }}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Срок: ${firstStepState.periodMonths.ifEmpty { "не введён" }}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                Toast.makeText(context, "ViewModel работает!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Проверить")
        }
    }
}