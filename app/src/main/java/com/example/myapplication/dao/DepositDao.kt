package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.entities.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)
    @Query("SELECT * FROM deposits ORDER BY date DESC")
    fun getAll(): Flow<List<DepositEntity>>
}