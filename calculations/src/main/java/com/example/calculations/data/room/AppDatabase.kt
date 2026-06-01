package com.example.calculations.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DepositCalculationEntity::class],
    version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao
}