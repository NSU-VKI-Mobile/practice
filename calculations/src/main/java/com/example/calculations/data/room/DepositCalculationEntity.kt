package com.example.calculations.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    /** стартовый взнос **/
    val initialAmount: Double,
    /** срок вклада **/
    val periodMonths: Int,
    /** процентная ставка **/
    val interestRate: Int,
    /** ежемесячное пополнение **/
    val monthlyTopUp: Double?,
    /** итоговая сумма **/
    val finalAmount: Double,
    /** начисленные проценты **/
    val interestEarned: Double,
    /** дата и время рассчета **/
    val calculationDate: Long
)