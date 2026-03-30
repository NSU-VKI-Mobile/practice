package ci.nsu.mobile.main.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositCalculationDao {
    @Insert
    suspend fun insert(entity: DepositCalculationEntity): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun observeAll(): LiveData<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id LIMIT 1")
    fun observeById(id: Long): LiveData<DepositCalculationEntity?>
}

