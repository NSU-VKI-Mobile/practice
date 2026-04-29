package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.mobile.main.ui.theme.PracticeTheme
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

