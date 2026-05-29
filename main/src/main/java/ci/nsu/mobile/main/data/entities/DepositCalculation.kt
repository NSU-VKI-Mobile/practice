package ci.nsu.mobile.main.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
//Через Room он автоматически превращается в таблицу SQLite.
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val initialAmount: Double,

    val periodMonths: Int,

    val interestRate: Double,

    val monthlyTopUp: Double,

    val finalAmount: Double,

    val interestEarned: Double,

    val calculationDate: Long
)