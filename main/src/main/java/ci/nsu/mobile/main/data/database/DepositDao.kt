package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ci.nsu.mobile.main.data.database.DepCalcs
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("select * from DepCal")
    fun getAll(): Flow<List<DepCalcs>>

    @Query("SELECT * FROM DepCal WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepCalcs?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepCalcs)

    @Delete
    suspend fun delete(calculation: DepCalcs)
}