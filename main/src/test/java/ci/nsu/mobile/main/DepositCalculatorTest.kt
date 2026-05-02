package ci.nsu.mobile.main

import ci.nsu.mobile.main.domain.DepositCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DepositCalculatorTest {
    @Test
    fun availableRatesMatchDepositPeriod() {
        assertEquals(listOf(15.0), DepositCalculator.availableRates(5))
        assertEquals(listOf(10.0), DepositCalculator.availableRates(6))
        assertEquals(listOf(5.0), DepositCalculator.availableRates(12))
        assertTrue(DepositCalculator.availableRates(0).isEmpty())
    }

    @Test
    fun calculateUsesMonthlyCapitalizationAndTopUps() {
        val result = DepositCalculator.calculate(
            initialAmount = 10_000.0,
            periodMonths = 12,
            interestRate = 5.0,
            monthlyTopUp = 1_000.0
        )

        assertEquals(10_000.0, result.initialAmount, 0.0)
        assertEquals(12, result.periodMonths)
        assertEquals(5.0, result.interestRate, 0.0)
        assertEquals(1_000.0, result.monthlyTopUp ?: 0.0, 0.0)
        assertEquals(22_790.47, result.finalAmount, 0.0)
        assertEquals(790.47, result.interestEarned, 0.0)
    }
}
