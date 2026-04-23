package ci.nsu.moble.main

import android.app.Application

class DepositApp : Application() {
    val repository: DepositRepository by lazy {
        val db = AppDatabase.getDatabase(this)
        DepositRepository(db.depositDao())
    }
}