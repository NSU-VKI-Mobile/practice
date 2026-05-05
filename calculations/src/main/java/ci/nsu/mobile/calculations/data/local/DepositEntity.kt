package ci.nsu.mobile.calculations.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ci.nsu.mobile.domain.model.DepositCalculation

@Entity(tableName = "deposit_calculations")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?, // Может быть null, так как поле необязательное
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis() // Время сохранения
) {
    fun toDomain(): DepositCalculation {
        return DepositCalculation(
            id = id,
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = calculationDate
        )
    }

    companion object {
        fun fromDomain(calculation: DepositCalculation): DepositEntity {
            return DepositEntity(
                id = calculation.id,
                userId = calculation.userId,
                initialAmount = calculation.initialAmount,
                periodMonths = calculation.periodMonths,
                interestRate = calculation.interestRate,
                monthlyTopUp = calculation.monthlyTopUp,
                finalAmount = calculation.finalAmount,
                interestEarned = calculation.interestEarned,
                calculationDate = calculation.calculationDate
            )
        }
    }
}
