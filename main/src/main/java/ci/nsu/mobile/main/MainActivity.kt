package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.main.data.DepositDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.ui.navigation.AppNavigation
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DepositDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(db.depositDao())
        val viewModel = DepositViewModel(repository)

        setContent {
            MaterialTheme {
                AppNavigation(viewModel)
            }
        }
    }
}