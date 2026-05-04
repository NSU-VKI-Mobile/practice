package ci.nsu.mobile.main.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.data.entity.DepositCalculation

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): LiveData<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    fun getById(id: Long): LiveData<DepositCalculation?>

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}
