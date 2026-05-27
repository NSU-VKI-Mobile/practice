package ci.nsu.mobile.calculations.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ci.nsu.mobile.domain.calculations.DepositCalculation

@Entity(tableName = "deposit_calculations", indices = [Index(value = ["userId"])])
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
) {
    // Конвертация Entity > Domain
    fun toDomain(): DepositCalculation = DepositCalculation(
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

    companion object {
        // Конвертация Domain > Entity
        fun fromDomain(calculation: DepositCalculation): DepositCalculationEntity =
            DepositCalculationEntity(
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