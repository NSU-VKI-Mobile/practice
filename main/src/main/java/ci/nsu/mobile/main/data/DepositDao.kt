package ci.nsu.mobile.main.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(item: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY id DESC")
    fun getAll(): LiveData<List<DepositCalculation>>

    @Delete
    suspend fun delete(item: DepositCalculation)
}