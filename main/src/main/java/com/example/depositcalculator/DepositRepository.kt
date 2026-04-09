package com.example.depositcalculator.repository

import com.example.depositcalculator.DepositDao
import com.example.depositcalculator.DepositEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val dao: DepositDao
){
    suspend fun insert(deposit: DepositEntity){
        dao.insert(deposit)
    }

    fun getAll(): Flow<List<DepositEntity>>{
        return dao.getAll()
    }
}