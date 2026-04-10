package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository

class DepositApp : Application() {

    // Инициализируется один раз при запуске приложения
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository: DepositRepository by lazy {
        DepositRepository(database.depositDao())
    }
}