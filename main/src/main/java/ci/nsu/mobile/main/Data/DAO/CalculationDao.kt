package ci.nsu.mobile.main.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Insert
    suspend fun insert(calculation: CalculationEntity)

    @Query("SELECT * FROM calculations ORDER BY timestamp DESC")
    fun getAllCalculations(): Flow<List<CalculationEntity>>

    @Query("DELETE FROM calculations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM calculations WHERE id = :id")
    fun getCalculationById(id: Long): Flow<CalculationEntity?>
}