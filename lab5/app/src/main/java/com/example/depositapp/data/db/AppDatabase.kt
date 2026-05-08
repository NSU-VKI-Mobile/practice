package com.example.depositapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// @Database — говорит Room что это главный класс базы данных
// entities — список всех таблиц (у нас одна)
// version — версия базы. Если меняем структуру таблицы — увеличиваем
// exportSchema = false — не экспортировать схему в файл (для простоты)
@Database(
    entities = [DepositCalculation::class],
    version = 1,
    exportSchema = false
)
// abstract class — Room сам напишет реализацию, нам нужен только интерфейс
abstract class AppDatabase : RoomDatabase() {

    // Абстрактная функция — Room создаст реальный DAO за нас
    abstract fun depositDao(): DepositDao

    // companion object — это как static в Java/Python
    // Всё что внутри — принадлежит классу, а не объекту
    companion object {

        // @Volatile — переменная видна всем потокам сразу (без кэширования)
        // Важно при многопоточности
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Паттерн Синглтон — гарантируем что база создаётся ОДИН раз
        fun getDatabase(context: Context): AppDatabase {
            // Если база уже создана — вернуть её
            // ?: — оператор "элвис": если слева null, выполни справа
            return INSTANCE ?: synchronized(this) {
                // synchronized — только один поток может зайти сюда одновременно
                // Защита от создания двух баз параллельно
                Room.databaseBuilder(
                    context.applicationContext, // контекст приложения (не Activity!)
                    AppDatabase::class.java,    // класс нашей базы
                    "deposits_db"               // имя файла базы данных
                ).build().also { INSTANCE = it } // сохраняем в INSTANCE и возвращаем
            }
        }
    }
}
