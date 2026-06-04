package ci.nsu.mobile.calculations

import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.entity.DepositEntity
import ci.nsu.mobile.domain.model.DepositCalculation
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class CalculationsProviderImplTest {

    @MockK
    private lateinit var dao: DepositDao

    private lateinit var provider: CalculationsProviderImpl

    private fun domainCalc(
        id: Int = 0,
        userId: Int = 1,
        startAmount: Double = 5_000.0,
        months: Int = 6,
        rate: Double = 8.0,
        monthlyTopUp: Double? = 300.0,
        finalAmount: Double = 8_500.0,
        profit: Double = 2_000.0,
        date: Long = 1_700_000_000_000L
    ) = DepositCalculation(
        id = id, userId = userId, startAmount = startAmount, months = months,
        rate = rate, monthlyTopUp = monthlyTopUp, finalAmount = finalAmount,
        profit = profit, date = date
    )

    private fun entity(
        id: Int = 0,
        userId: Int = 1,
        startAmount: Double = 5_000.0,
        months: Int = 6,
        rate: Double = 8.0,
        monthlyTopUp: Double? = 300.0,
        finalAmount: Double = 8_500.0,
        profit: Double = 2_000.0,
        date: Long = 1_700_000_000_000L
    ) = DepositEntity(
        id = id, userId = userId, startAmount = startAmount, months = months,
        rate = rate, monthlyTopUp = monthlyTopUp, finalAmount = finalAmount,
        profit = profit, date = date
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        provider = CalculationsProviderImpl(dao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when getCalculationsForUser called then dao getByUser is invoked`() = runTest {
        val userId = 1
        every { dao.getByUser(userId) } returns flowOf(emptyList())

        provider.getCalculationsForUser(userId).first()

        verify(exactly = 1) { dao.getByUser(userId) }
    }

    @Test
    fun `when getCalculationsForUser called then returns mapped domain list`() = runTest {
        val userId = 1
        every { dao.getByUser(userId) } returns flowOf(
            listOf(entity(id = 1), entity(id = 2))
        )

        val result = provider.getCalculationsForUser(userId).first()

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }

    @Test
    fun `when user has no calculations then flow emits empty list`() = runTest {
        every { dao.getByUser(99) } returns flowOf(emptyList())

        val result = provider.getCalculationsForUser(99).first()

        assertEquals(0, result.size)
    }

    @Test
    fun `when getCalculationsForUser called then all entity fields are preserved in domain`() = runTest {
        val e = entity(id = 5, userId = 2, startAmount = 12_000.0, months = 18, rate = 7.5,
            monthlyTopUp = 1_000.0, finalAmount = 30_000.0, profit = 8_000.0, date = 12345L)
        every { dao.getByUser(2) } returns flowOf(listOf(e))

        val domain = provider.getCalculationsForUser(2).first().first()

        assertEquals(e.id,           domain.id)
        assertEquals(e.userId,       domain.userId)
        assertEquals(e.startAmount,  domain.startAmount)
        assertEquals(e.months,       domain.months)
        assertEquals(e.rate,         domain.rate)
        assertEquals(e.monthlyTopUp, domain.monthlyTopUp)
        assertEquals(e.finalAmount,  domain.finalAmount)
        assertEquals(e.profit,       domain.profit)
        assertEquals(e.date,         domain.date)
    }

    @Test
    fun `when saveCalculation called then dao insert is invoked exactly once`() = runTest {
        val calc = domainCalc()
        coEvery { dao.insert(any()) } just Runs

        provider.saveCalculation(calc)

        coVerify(exactly = 1) { dao.insert(any()) }
    }

    @Test
    fun `when saveCalculation called then inserted entity preserves all fields`() = runTest {
        val calc = domainCalc(id = 3, userId = 5, startAmount = 20_000.0, months = 24,
            rate = 9.5, monthlyTopUp = 2_000.0, finalAmount = 60_000.0,
            profit = 14_000.0, date = 99999L)
        val captured = slot<DepositEntity>()
        coEvery { dao.insert(capture(captured)) } just Runs

        provider.saveCalculation(calc)

        with(captured.captured) {
            assertEquals(calc.id,           id)
            assertEquals(calc.userId,       userId)
            assertEquals(calc.startAmount,  startAmount)
            assertEquals(calc.months,       months)
            assertEquals(calc.rate,         rate)
            assertEquals(calc.monthlyTopUp, monthlyTopUp)
            assertEquals(calc.finalAmount,  finalAmount)
            assertEquals(calc.profit,       profit)
            assertEquals(calc.date,         date)
        }
    }

    @Test
    fun `when saveCalculation called with null monthlyTopUp then entity also has null`() = runTest {
        val calc = domainCalc(monthlyTopUp = null)
        val captured = slot<DepositEntity>()
        coEvery { dao.insert(capture(captured)) } just Runs

        provider.saveCalculation(calc)

        assertNull(captured.captured.monthlyTopUp)
    }

    @Test
    fun `when saveCalculation called twice then dao insert is invoked twice`() = runTest {
        coEvery { dao.insert(any()) } just Runs

        provider.saveCalculation(domainCalc(id = 1))
        provider.saveCalculation(domainCalc(id = 2))

        coVerify(exactly = 2) { dao.insert(any()) }
    }

    @Test
    fun `when deleteCalculation called then dao deleteById is invoked with correct id`() = runTest {
        val id = 42
        coEvery { dao.deleteById(id) } just Runs

        provider.deleteCalculation(id)

        coVerify(exactly = 1) { dao.deleteById(id) }
    }

    @Test
    fun `when deleteCalculation called then insert is not triggered`() = runTest {
        coEvery { dao.deleteById(any()) } just Runs

        provider.deleteCalculation(1)

        coVerify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `when deleteCalculation called with different ids then each id is passed to dao`() = runTest {
        coEvery { dao.deleteById(any()) } just Runs

        provider.deleteCalculation(10)
        provider.deleteCalculation(20)

        coVerify(exactly = 1) { dao.deleteById(10) }
        coVerify(exactly = 1) { dao.deleteById(20) }
    }
}