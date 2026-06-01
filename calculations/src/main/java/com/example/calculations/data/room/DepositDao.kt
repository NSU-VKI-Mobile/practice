package com.example.calculations.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun InsertDeposit(deposit: DepositCalculationEntity)

    @Query("select * from deposit_calculations where userId=:userId order by calculationDate desc")
    fun GetAll(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("select * from deposit_calculations where initialAmount=:initialAmount and periodMonths=:periodMonths and interestRate=:interestRate  AND ((monthlyTopUp IS NULL AND :monthlyTopUp IS NULL) OR monthlyTopUp = :monthlyTopUp) LIMIT 1")
    suspend fun findDuplication(initialAmount: Double, periodMonths: Int, interestRate: Int, monthlyTopUp: Double?): DepositCalculationEntity?
    @Query("""
    SELECT * FROM deposit_calculations 
    WHERE userId = :userId
    AND (:amountStart IS NULL OR finalAmount >= :amountStart)
    AND (:amountFinish IS NULL OR finalAmount <= :amountFinish)
    AND (:date IS NULL OR calculationDate >= :date)
    AND (:rate IS NULL OR interestRate = :rate)
    ORDER BY calculationDate DESC
""")
    fun GetFiltered(
        userId: Long,
        amountStart: Double?,
        amountFinish: Double?,
        date: Long?,
        rate: Int?
    ): Flow<List<DepositCalculationEntity>>
    @Delete
    suspend fun deleteDeposit(deposit: DepositCalculationEntity)

    @Query("DELETE FROM deposit_calculations WHERE id = :depositId")
    suspend fun deleteDepositById(depositId: Long)
}