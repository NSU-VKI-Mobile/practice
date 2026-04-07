package ci.nsu.mobile.main.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.data.model.DepositCalculation

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAll(): LiveData<List<DepositCalculation>>
}