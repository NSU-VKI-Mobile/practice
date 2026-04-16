// MainActivity.kt
package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.screens.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase
    private lateinit var repository: DepositRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        repository = DepositRepository(database.depositDao())

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation(repository = repository)
                }
            }
        }
    }
}