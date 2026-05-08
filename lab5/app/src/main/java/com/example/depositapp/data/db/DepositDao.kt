package com.example.depositapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// @Dao — Data Access Object
// Это интерфейс (не класс!) — Room сам напишет реализацию за нас
// Здесь описываем ЧТО мы хотим делать с базой данных
@Dao
interface DepositDao {

    // @Insert — Room автоматически напишет INSERT INTO deposit_calculations ...
    // suspend — функция асинхронная, нельзя вызывать из основного потока
    // onConflict = REPLACE — если запись с таким id уже есть, заменить её
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    // @Query — пишем SQL-запрос вручную
    // SELECT * FROM — выбрать все колонки
    // ORDER BY calculationDate DESC — сортировка по дате, новые сначала
    // Flow<List<...>> — поток данных: при каждом изменении таблицы
    // UI автоматически получит обновлённый список (как StateFlow)
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    // Получить одну запись по id
    // suspend — асинхронная, ждём результат
    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculation?
}
