package ci.nsu.mobile.calculations.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double = 0.0,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): ci.nsu.mobile.domain.model.DepositCalculation {
        return ci.nsu.mobile.domain.model.DepositCalculation(
            id = this.id,
            userId = this.userId,
            initialAmount = this.initialAmount,
            periodMonths = this.periodMonths,
            interestRate = this.interestRate,
            monthlyTopUp = this.monthlyTopUp,
            finalAmount = this.finalAmount,
            interestEarned = this.interestEarned,
            calculationDate = this.calculationDate
        )
    }
}

fun ci.nsu.mobile.domain.model.DepositCalculation.toEntity(): DepositCalculationEntity {
    return DepositCalculationEntity(
        id = this.id,
        userId = this.userId,
        initialAmount = this.initialAmount,
        periodMonths = this.periodMonths,
        interestRate = this.interestRate,
        monthlyTopUp = this.monthlyTopUp,
        finalAmount = this.finalAmount,
        interestEarned = this.interestEarned,
        calculationDate = this.calculationDate
    )
}


@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    @Delete
    suspend fun delete(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculationEntity>>
}

@Database(entities = [DepositCalculationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposit_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}