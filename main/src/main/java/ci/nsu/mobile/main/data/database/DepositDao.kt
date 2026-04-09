package ci.nsu.mobile.main.data.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculation ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("DELETE FROM deposit_calculation")
    suspend fun deleteAll()
}