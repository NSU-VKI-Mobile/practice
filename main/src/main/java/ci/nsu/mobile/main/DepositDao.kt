package ci.nsu.mobile.main

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
//Интерфейс запросов
@Dao //этот интерфейс будет DAO (метка "это пульт управления БД")
//interface — это набор команд без реализации (просто список того, что можно делать)
interface DepositDao {
    @Insert //Room сам напишет код для вставки
    //suspend — функция может долго работать (не блокирует интерфейс)
    suspend fun insert(calculation: DepositCalculation): Long //принимает объект для сохранения
    //Query - SQL-запрос
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    //ORDER BY calculationDate DESC — сортировать по дате (сначала новые)
    fun getAllCalculations(): Flow<List<DepositCalculation>>
    //Flow<List<DepositCalculation>> — возвращает поток списка записей
    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    //:id — подставляем параметр функции
    //DepositCalculation? — может вернуть запись или null (если не найдена)
    suspend fun getCalculationById(id: Long): DepositCalculation?

    @Delete
    suspend fun delete(calculation: DepositCalculation)
}