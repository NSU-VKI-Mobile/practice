package com.example.integratedapp.data.repository

import com.example.integratedapp.data.db.AppDatabase
import com.example.integratedapp.data.db.DepositCalculation
import kotlinx.coroutines.flow.Flow

// Repository для расчётов — работает с локальной базой Room
class DepositRepository(db: AppDatabase) {

    private val dao = db.depositDao()

    suspend fun save(calculation: DepositCalculation) = dao.insert(calculation)

    suspend fun delete(calculation: DepositCalculation) = dao.delete(calculation)

    // Получить расчёты конкретного пользователя
    // Принимает userId — будем брать его из SessionManager во ViewModel
    fun getByUser(userId: Long): Flow<List<DepositCalculation>> = dao.getByUser(userId)

    suspend fun getById(id: Long): DepositCalculation? = dao.getById(id)
}
