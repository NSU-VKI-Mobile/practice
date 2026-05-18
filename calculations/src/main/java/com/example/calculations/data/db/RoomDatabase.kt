package com.example.calculations.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calculations.data.dbo.Deposit
import com.example.calculations.data.dbo.DepositDao


@Database(entities = [(Deposit::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database_name"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}