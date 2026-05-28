package com.example.calculations.data.dba

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "deposits")
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long // timestamp
)

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposits")
    fun getDeposits() : LiveData<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId == :userId")
    fun getDepositsUser(userId: Long): Flow<List<Deposit>>

    @Query("DELETE FROM deposits WHERE id = :calculationId")
    suspend fun deleteById(calculationId: Long)
    @Insert
    fun addDeposit(deposit: Deposit)
}