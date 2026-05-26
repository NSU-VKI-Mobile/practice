package ci.nsu.moble.calculations.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ci.nsu.moble.domain.models.DepositCalculation
import java.util.Date

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val profit: Double,
    val calculationDate: Date
) {
    fun toDomain() = DepositCalculation(
        id = id,
        userId = userId,
        initialAmount = initialAmount,
        months = months,
        rate = rate,
        monthlyTopUp = monthlyTopUp,
        finalAmount = finalAmount,
        profit = profit,
        calculationDate = calculationDate
    )

    companion object {
        fun fromDomain(calculation: DepositCalculation) = DepositEntity(
            id = calculation.id,
            userId = calculation.userId,
            initialAmount = calculation.initialAmount,
            months = calculation.months,
            rate = calculation.rate,
            monthlyTopUp = calculation.monthlyTopUp,
            finalAmount = calculation.finalAmount,
            profit = calculation.profit,
            calculationDate = calculation.calculationDate
        )
    }
}