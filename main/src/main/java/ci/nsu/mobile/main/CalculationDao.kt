package ci.nsu.mobile.main

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculations ORDER BY date DESC")
    fun getAllCalculations(): Flow<List<Calculation>>

    @Query("SELECT * FROM calculations ORDER BY date DESC")
    suspend fun getAllCalculationsList(): List<Calculation>

    @Query("SELECT * FROM calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): Calculation?

    @Insert
    suspend fun insertCalculation(calculation: Calculation)

    @Delete
    suspend fun deleteCalculation(calculation: Calculation)

    @Query("DELETE FROM calculations")
    suspend fun deleteAllCalculations()
}