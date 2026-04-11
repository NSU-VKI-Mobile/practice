package ci.nsu.mobile.main.DBO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "deposits")
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long // timestamp
)

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposits")
    fun getDeposits(): LiveData<List<Deposit>>

    @Insert
    fun addDeposit(deposit: Deposit)
}