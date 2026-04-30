package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.database.AppDatabase
import ci.nsu.mobile.main.repository.DepositRepository

class DepositApplication : Application() {

    lateinit var repository: DepositRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        val database = AppDatabase.getDatabase(this)
        repository = DepositRepository.getInstance(database)
    }

    companion object {
        private lateinit var instance: DepositApplication

        fun getInstance(): DepositApplication {
            return instance
        }
    }
}