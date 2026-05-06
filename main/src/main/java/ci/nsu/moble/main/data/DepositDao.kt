package ci.nsu.moble.main.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Query("SELECT * FROM deposits ORDER BY date DESC")
    fun getAllCalculations(): LiveData<List<DepositEntity>>
}