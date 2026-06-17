package ci.nsu.mobile.calculations.data

import android.content.Context
import androidx.room.*
import ci.nsu.mobile.domain.model.DepositCalculation as DomainCalculation
import kotlinx.coroutines.flow.Flow

// 1. Сущность БД
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
    fun toDomainModel(): DomainCalculation {
        return DomainCalculation(
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

// Функция конвертации из Domain модели в Entity
fun DomainCalculation.toEntity(): DepositCalculationEntity {
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

// 2. Интерфейс DAO
@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    @Delete
    suspend fun delete(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)
}

// 3. Класс Базы Данных
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