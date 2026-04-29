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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.roomDatabase.AppDatabase
import ci.nsu.mobile.main.data.roomDatabase.DepositDao
import ci.nsu.mobile.main.ui.navigation.NavGraph
import ci.nsu.mobile.main.viewModel.DepositViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database: AppDatabase = AppDatabase.getDatabase(this)
        val dao: DepositDao = database.depositDao()
        val repository = DepositRepository(dao)
        val viewModel = DepositViewModel(repository)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                NavGraph(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

