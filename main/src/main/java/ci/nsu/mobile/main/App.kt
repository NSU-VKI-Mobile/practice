package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository

class App : Application() {

    lateinit var repository: DepositRepository

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getDatabase(this)
        repository = DepositRepository(database.depositDao())
    }
}
