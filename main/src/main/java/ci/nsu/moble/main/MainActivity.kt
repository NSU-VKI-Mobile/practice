package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.moble.main.viewmodel.DepositViewModel
import ci.nsu.moble.main.navigation.NavGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repository = DepositRepository(db.depositDao())
        val viewModel = DepositViewModel(repository)

        setContent {
            NavGraph(viewModel)
        }
    }
}