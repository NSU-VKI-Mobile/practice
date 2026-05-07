package ci.nsu.mobile.main

import ci.nsu.mobile.calculations.ui.DepositCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class DepositCalculatorTest {
    private val calculator = DepositCalculator()

    @Test
    fun `when valid parameters without top-up then calculation is correct`() {
        val result = calculator.calculate(
            initialAmount = 10000.0,
            periodMonths = 12,
            interestRate = 12.0,
            monthlyTopUp = 0.0
        )
        assertEquals(11268.25, result.finalAmount, 0.01)
        assertEquals(1268.25, result.interestEarned, 0.01)
    }

    @Test
    fun `when monthly top-up then final amount includes top-ups`() {
        val result = calculator.calculate(
            initialAmount = 10000.0,
            periodMonths = 12,
            interestRate = 12.0,
            monthlyTopUp = 1000.0
        )
        assertEquals(24077.58, result.finalAmount, 0.01)
    }

    @Test
    fun `when zero months then only initial amount returned`() {
        val result = calculator.calculate(10000.0, 0, 12.0, 0.0)
        assertEquals(10000.0, result.finalAmount, 0.01)
        assertEquals(0.0, result.interestEarned, 0.01)
    }

    @Test
    fun `when zero interest rate then amount stays same`() {
        val result = calculator.calculate(10000.0, 12, 0.0, 0.0)
        assertEquals(10000.0, result.finalAmount, 0.01)
    }
}