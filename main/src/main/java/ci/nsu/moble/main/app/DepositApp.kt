package ci.nsu.moble.main.app

import android.app.Application
import ci.nsu.moble.main.data.database.AppDatabase
import ci.nsu.moble.main.data.repository.DepositRepository

class DepositApp : Application() {
    val repository: DepositRepository by lazy {
        val db = AppDatabase.getDatabase(this)
        DepositRepository(db.depositDao())
    }
}