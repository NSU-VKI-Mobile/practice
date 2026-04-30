package ci.nsu.moble.main

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposits ORDER BY id DESC")
    fun getAll(): LiveData<List<DepositCalculation>>
}
