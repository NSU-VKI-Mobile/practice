package com.example.depositcalculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    fun getCalculationById(id: Long): Flow<DepositCalculationEntity?>
}