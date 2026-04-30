package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val months: Int,
    val rate: Double,
    val monthlyAdd: Double,
    val total: Double,
    val interest: Double,
    val date: Long = System.currentTimeMillis()
)