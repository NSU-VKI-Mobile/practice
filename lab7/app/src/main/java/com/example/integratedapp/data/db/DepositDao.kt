package com.example.integratedapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Delete
    suspend fun delete(calculation: DepositCalculation)

    // Получить расчёты только конкретного пользователя
    // :userId — параметр SQL, Room сам подставит значение из аргумента функции
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getByUser(userId: Long): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculation?
}
