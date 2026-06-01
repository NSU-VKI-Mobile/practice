package com.example.domain.models

data class DepositFilter(
    val amountStart: Double? = null,
    val amountFinish: Double? = null,
    val date: Long? = null,
    val rate: Int? = null
)