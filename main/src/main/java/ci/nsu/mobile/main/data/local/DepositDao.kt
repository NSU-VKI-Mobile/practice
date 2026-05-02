package ci.nsu.mobile.main.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Query(
        """
        SELECT * FROM deposit_calculations
        WHERE userId = :userId
            AND (:minAmount IS NULL OR initialAmount >= :minAmount)
            AND (:maxAmount IS NULL OR initialAmount <= :maxAmount)
            AND (:fromDate IS NULL OR calculationDate >= :fromDate)
            AND (:toDate IS NULL OR calculationDate <= :toDate)
        ORDER BY calculationDate DESC
        """
    )
    fun getCalculationsForUser(
        userId: Long,
        minAmount: Double?,
        maxAmount: Double?,
        fromDate: Long?,
        toDate: Long?
    ): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id AND userId = :userId LIMIT 1")
    fun getCalculationByIdForUser(id: Long, userId: Long): Flow<DepositCalculation?>

    @Query("DELETE FROM deposit_calculations WHERE id = :id AND userId = :userId")
    suspend fun deleteCalculationByIdForUser(id: Long, userId: Long)

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculation)
}
