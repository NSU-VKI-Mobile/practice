package ci.nsu.mobile.calculations.data

import android.content.Context
import androidx.room.*
import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)

fun DepositCalculationEntity.toDomain() = DepositCalculation(
    id, userId, initialAmount, periodMonths, interestRate, monthlyTopUp, finalAmount, interestEarned, calculationDate
)

fun DepositCalculation.toEntity() = DepositCalculationEntity(
    id, userId, initialAmount, periodMonths, interestRate, monthlyTopUp, finalAmount, interestEarned, calculationDate
)

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getHistoryForUser(userId: Int): Flow<List<DepositCalculationEntity>>

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun delete(id: Long)
}

@Database(entities = [DepositCalculationEntity::class], version = 2, exportSchema = false)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: DepositDatabase? = null

        fun getDatabase(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposits_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DepositRepository(private val depositDao: DepositDao) {
    fun getHistory(userId: Int): Flow<List<DepositCalculation>> =
        depositDao.getHistoryForUser(userId).map { list -> list.map { it.toDomain() } }

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insert(calculation.toEntity())
    }

    suspend fun delete(id: Long) {
        depositDao.delete(id)
    }
}