package mobile

import android.app.Application
import androidx.room.Room
import mobile.data.AppDatabase
import mobile.domain.DepositRepository

class DepositApp : Application() {
    // Создаем базу данных только тогда, когда она реально понадобится (lazy)
    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "deposit_database"
        )
            .fallbackToDestructiveMigration() // <--- ДОБАВЬ ЭТО!
            .build()
    }

    // Создаем репозиторий, передавая ему DAO из базы данных
    val repository by lazy {
        DepositRepository(database.depositDao())
    }
}