package data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    //метод вставки
    @Insert
    suspend fun insertDeposit(param: DepositCalculations)

    //метод получения всех записей
    @Query("SELECT * FROM deposit_calculations order by calculationDate desc")
    fun getAllDeposit(): Flow<List<DepositCalculations>> //Flow - автоматически отпр. обновленный списко всем подписчикам

    //метод получения 1 записи по ид
    @Query("select * from deposit_calculations where id = :id")
    suspend fun getDepositById(id: Long): DepositCalculations?

}