// Task_5: DAO для работы с таблицей deposit_calculations.

package ci.nsu.moble.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    suspend fun getAll(): List<DepositCalculation>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculation?
}
