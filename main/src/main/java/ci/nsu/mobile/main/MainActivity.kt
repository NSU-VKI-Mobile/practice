package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.ui.DepositApp
import ci.nsu.mobile.main.ui.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

// Для передачи ViewModel через композицию (опционально)
val LocalRepository = staticCompositionLocalOf<DepositRepository?> { null }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация базы данных и репозитория
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    // Передаём repository через CompositionLocal
                    CompositionLocalProvider(LocalRepository provides repository) {
                        DepositApp(navController = navController)
                    }
                }
            }
        }
    }
}

// Factory для ViewModel с зависимостью от Repository
class ViewModelFactory(private val repository: DepositRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}