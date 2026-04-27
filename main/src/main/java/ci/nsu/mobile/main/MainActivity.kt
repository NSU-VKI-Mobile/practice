package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ci.nsu.mobile.main.ViewModel.DepositRepository
import ci.nsu.mobile.main.Data.Local.AppDatabase
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import ci.nsu.mobile.main.ui.AppNavigation
import ci.nsu.mobile.main.ui.theme.PracticeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())

        val viewModel = DepositViewModel(repository)

        setContent {
            PracticeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(viewModel)
                }
            }
        }
    }
}