package ci.nsu.mobile.calculations

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DepositCalculationLogicTest {

    data class DepositResult(val finalAmount: Double, val profit: Double)

    private fun calculate(
        startAmount: Double,
        months: Int,
        rate: Double,
        monthlyTopUp: Double = 0.0
    ): DepositResult {
        var total = startAmount
        repeat(months) { total = (total + monthlyTopUp) * (1 + rate / 100.0) }
        val profit = total - (startAmount + monthlyTopUp * months)
        return DepositResult(finalAmount = total, profit = profit)
    }

    @Test
    fun `when 1 month no top-up then final amount equals start times rate factor`() {
        // 10 000 × 1.10 = 11 000
        val r = calculate(startAmount = 10_000.0, months = 1, rate = 10.0)
        assertEquals(11_000.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when 2 months no top-up then compound interest is applied twice`() {
        // 10 000 × 1.10² = 12 100
        val r = calculate(startAmount = 10_000.0, months = 2, rate = 10.0)
        assertEquals(12_100.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when 3 months no top-up then compound growth matches power formula`() {
        // 1 000 × 2³ = 8 000
        val r = calculate(startAmount = 1_000.0, months = 3, rate = 100.0)
        assertEquals(8_000.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when 1 month with top-up then top-up is added before interest`() {
        // (1 000 + 500) × 1.10 = 1 650
        val r = calculate(startAmount = 1_000.0, months = 1, rate = 10.0, monthlyTopUp = 500.0)
        assertEquals(1_650.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when 2 months with top-up then each month top-up and interest compound correctly`() {
        // m1: (1 000+500)×1.10 = 1 650
        // m2: (1 650+500)×1.10 = 2 365
        val r = calculate(startAmount = 1_000.0, months = 2, rate = 10.0, monthlyTopUp = 500.0)
        assertEquals(2_365.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when rate is 0 then final amount equals start plus all top-ups accumulated`() {
        val r = calculate(startAmount = 10_000.0, months = 12, rate = 0.0, monthlyTopUp = 1_000.0)
        assertEquals(22_000.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when months is 0 then final amount equals the start amount unchanged`() {
        val r = calculate(startAmount = 10_000.0, months = 0, rate = 10.0)
        assertEquals(10_000.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when start amount is 0 and only top-ups provided then calculation is based on top-ups alone`() {
        // m1: (0+500)×1.10 = 550
        val r = calculate(startAmount = 0.0, months = 1, rate = 10.0, monthlyTopUp = 500.0)
        assertEquals(550.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when rate is very small then result is slightly above principal`() {
        val r = calculate(startAmount = 1_000.0, months = 1, rate = 0.001)
        assertTrue(r.finalAmount > 1_000.0)
        assertTrue(r.finalAmount < 1_001.0)
    }

    @Test
    fun `when profit calculated then it equals final minus principal and all top-ups`() {
        // (10 000+1 000)×1.10 = 12 100 ; profit = 12 100 − (10 000 + 1×1 000) = 1 100
        val r = calculate(startAmount = 10_000.0, months = 1, rate = 10.0, monthlyTopUp = 1_000.0)
        assertEquals(12_100.0, r.finalAmount, 0.01)
        assertEquals(1_100.0, r.profit, 0.01)
    }

    @Test
    fun `when no top-up then profit equals final amount minus start amount`() {
        val r = calculate(startAmount = 1_000.0, months = 1, rate = 10.0)
        assertEquals(r.finalAmount - 1_000.0, r.profit, 0.01)
    }

    @Test
    fun `when rate is 0 then profit is 0`() {
        val r = calculate(startAmount = 5_000.0, months = 6, rate = 0.0, monthlyTopUp = 200.0)
        assertEquals(0.0, r.profit, 0.01)
    }

    @Test
    fun `when positive rate then profit is always positive`() {
        val r = calculate(startAmount = 5_000.0, months = 6, rate = 5.0, monthlyTopUp = 200.0)
        assertTrue(r.profit > 0.0)
    }

    private fun resolveRate(months: Int): List<Double> = when {
        months < 6  -> listOf(15.0)
        months < 12 -> listOf(10.0)
        else        -> listOf(5.0)
    }

    @Test
    fun `when months is 1 then rate tier is 15 (minimum boundary)`() {
        assertEquals(listOf(15.0), resolveRate(1))
    }

    @Test
    fun `when months is 5 then rate tier is 15 (just below 6)`() {
        assertEquals(listOf(15.0), resolveRate(5))
    }

    @Test
    fun `when months is 6 then rate tier switches to 10`() {
        assertEquals(listOf(10.0), resolveRate(6))
    }

    @Test
    fun `when months is 11 then rate tier is 10 (just below 12)`() {
        assertEquals(listOf(10.0), resolveRate(11))
    }

    @Test
    fun `when months is 12 then rate tier switches to 5`() {
        assertEquals(listOf(5.0), resolveRate(12))
    }

    @Test
    fun `when months is 60 then rate tier is 5 (long-term deposit)`() {
        assertEquals(listOf(5.0), resolveRate(60))
    }

    @Test
    fun `when short-term deposit uses 15 percent rate then final amount is higher than mid-term rate`() {
        val shortTermResult = calculate(10_000.0, months = 3, rate = 15.0)
        val midTermResult   = calculate(10_000.0, months = 3, rate = 10.0)
        assertTrue(shortTermResult.finalAmount > midTermResult.finalAmount)
    }
}