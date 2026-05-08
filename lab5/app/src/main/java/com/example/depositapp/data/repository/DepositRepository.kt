package com.example.depositapp.data.repository

import com.example.depositapp.data.db.AppDatabase
import com.example.depositapp.data.db.DepositCalculation
import kotlinx.coroutines.flow.Flow

// Repository — прослойка между ViewModel и базой данных
// ViewModel не знает существует ли Room, файл или сеть
// Она просто просит Repository: "дай мне данные"
//
// Зачем это нужно:
// 1. ViewModel не зависит от конкретной реализации хранилища
// 2. Легко поменять Room на что-то другое не трогая ViewModel
// 3. Можно комбинировать данные из нескольких источников

class DepositRepository(db: AppDatabase) {

    // Получаем DAO из базы данных
    // Через dao работаем с таблицей
    private val dao = db.depositDao()

    // Сохранить расчёт в базу
    // suspend — асинхронная операция (нельзя на главном потоке)
    suspend fun save(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    // Получить все расчёты — возвращает Flow (живой список)
    // Flow автоматически уведомит UI при любом изменении
    // Не suspend — Flow сам асинхронный
    fun getAll(): Flow<List<DepositCalculation>> {
        return dao.getAllCalculations()
    }

    // Получить один расчёт по id
    suspend fun getById(id: Long): DepositCalculation? {
        return dao.getById(id)
    }
}
