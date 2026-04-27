package ci.nsu.mobile.main.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startAmount: Double,
    val termMonths: Int,
    val rate: Double,
    val currency: String,
    val interest: Double,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)