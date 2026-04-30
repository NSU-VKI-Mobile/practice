package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.moble.main.data.dbo.AppDatabase
import ci.nsu.moble.main.ui.screens.DepositApp
import ci.nsu.moble.main.ui.theme.MobilepracticeTheme
import ci.nsu.moble.main.viewmodel.DepositViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация БД
        val db = AppDatabase.getDatabase(applicationContext)

        // Создание ViewModel вручную через Factory
        val viewModel: DepositViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DepositViewModel(db.dao()) as T
                }
            }
        }

        enableEdgeToEdge() // Content goes under status bar
        setContent {
            MobilepracticeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DepositApp(viewModel)
                }
            }
        }
    }
}
