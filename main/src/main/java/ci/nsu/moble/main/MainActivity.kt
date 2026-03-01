package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import ci.nsu.moble.main.data.DepositDatabase
import ci.nsu.moble.main.data.DepositRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            DepositDatabase::class.java,
            "deposit_db"
        ).build()

        val repository = DepositRepository(database.depositDao())

        setContent {
            MaterialTheme {
                AppNavGraph(
                    repository = repository,
                    onExitApp = { finish() }
                )
            }
        }
    }
}