package com.example.depositcalculator

import androidx.room.Entity
import  androidx.room.PrimaryKey
import java.time.temporal.TemporalAmount

@Entity(tableName = "deposit_calculations")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
)