package ci.nsu.mobile.main

import org.junit.Assert.assertEquals
import org.junit.Test

class DepositCalculator {
    fun calculate(initial: Double, percent: Double, months: Int): Double {
        if (initial < 0) throw IllegalArgumentException("Сумма не может быть отрицательной")
        return initial + (initial * (percent / 100) * (months / 12.0))
    }
}

class DepositCalculatorTest {

    private val calculator = DepositCalculator()

    @Test
    fun `when valid parameters then calculation is correct`() {
        val result = calculator.calculate(10000.0, 12.0, 12)
        assertEquals(11200.0, result, 0.01)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when negative amount then throw exception`() {
        calculator.calculate(-5000.0, 10.0, 12)
    }
}