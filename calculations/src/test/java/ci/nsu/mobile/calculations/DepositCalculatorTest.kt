package ci.nsu.mobile.calculations

import ci.nsu.mobile.calculations.domain.DepositCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class DepositCalculatorTest {
    @Test
    fun availableRatesDependOnPeriod() {
        assertEquals(listOf("15"), DepositCalculator.availableRates(3))
        assertEquals(listOf("10"), DepositCalculator.availableRates(6))
        assertEquals(listOf("5"), DepositCalculator.availableRates(12))
        assertEquals(emptyList<String>(), DepositCalculator.availableRates(0))
    }

    @Test
    fun calculateAppliesMonthlyCapitalizationAndTopUps() {
        val result = DepositCalculator.calculate(
            initialAmount = 1000.0,
            periodMonths = 2,
            interestRate = 12.0,
            monthlyTopUp = 100.0
        )

        assertEquals(1223.11, result.finalAmount, 0.01)
        assertEquals(23.11, result.interestEarned, 0.01)
    }
}
