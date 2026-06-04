package com.example.integratedapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity — таблица в базе SQLite
// В этой лабе добавлено поле userId — привязка расчёта к пользователю
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // ID пользователя — к нему привязан расчёт
    // Каждый пользователь видит только свои расчёты
    val userId: Long,

    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
)
