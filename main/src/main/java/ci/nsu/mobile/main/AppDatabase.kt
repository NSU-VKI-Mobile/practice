package ci.nsu.mobile.main

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DepositCalculation::class],
    version = 1,
    exportSchema = false //не сохранять схему в папку
)
abstract class AppDatabase : RoomDatabase() {
    //abstract — абстрактный класс (нельзя создать объект напрямую)
    abstract fun depositDao(): DepositDao
    //abstract fun — абстрактная функция (Room сам напишет реализацию)

    companion object { //companion object — объект, связанный с классом
        @Volatile //защита от проблем с многопоточностью
        private var INSTANCE: AppDatabase? = null //переменная для хранения единственного экземпляра
        //может быть null (ещё не создана)

        fun getDatabase(context: Context): AppDatabase { //Функция для получения базы данных
            return INSTANCE ?: synchronized(this) {
                //?: — оператор "если левая часть null, то делай правую"
                //synchronized(this) — блокировка от параллельного доступа
                val instance = Room.databaseBuilder( //строитель базы данных
                    context.applicationContext, //контекст приложения
                    AppDatabase::class.java, // класс базы
                    "deposits_database"
                ).build() //построить базу
                INSTANCE = instance //сохраняем экземпляр
                instance //возвращаем его
            }
        }
    }
}