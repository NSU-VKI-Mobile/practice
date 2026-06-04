package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.entity.DepositEntity
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DepositRepositoryTest {

    @MockK
    private lateinit var dao: DepositDao

    private lateinit var repository: DepositRepository

    private fun entity(
        id: Int = 0,
        userId: Int = 1,
        startAmount: Double = 10_000.0,
        months: Int = 12,
        rate: Double = 10.0,
        monthlyTopUp: Double? = 500.0,
        finalAmount: Double = 25_000.0,
        profit: Double = 9_000.0,
        date: Long = 1_700_000_000_000L
    ) = DepositEntity(
        id = id,
        userId = userId,
        startAmount = startAmount,
        months = months,
        rate = rate,
        monthlyTopUp = monthlyTopUp,
        finalAmount = finalAmount,
        profit = profit,
        date = date
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = DepositRepository(dao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when getByUser called then flow contains mapped domain objects`() = runTest {
        val userId = 1
        every { dao.getByUser(userId) } returns flowOf(listOf(entity(id = 1), entity(id = 2)))

        val result = repository.getByUser(userId).first()

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }

    @Test
    fun `when getByUser called for user with no deposits then returns empty list`() = runTest {
        val userId = 99
        every { dao.getByUser(userId) } returns flowOf(emptyList())

        val result = repository.getByUser(userId).first()

        assertEquals(0, result.size)
    }

    @Test
    fun `when getByUser called then all domain fields are correctly mapped`() = runTest {
        val userId = 1
        val e = entity(id = 3, userId = userId)
        every { dao.getByUser(userId) } returns flowOf(listOf(e))

        val domain = repository.getByUser(userId).first().first()

        assertEquals(e.id, domain.id)
        assertEquals(e.userId, domain.userId)
        assertEquals(e.startAmount, domain.startAmount)
        assertEquals(e.months, domain.months)
        assertEquals(e.rate, domain.rate)
        assertEquals(e.monthlyTopUp, domain.monthlyTopUp)
        assertEquals(e.finalAmount, domain.finalAmount)
        assertEquals(e.profit, domain.profit)
        assertEquals(e.date, domain.date)
    }

    @Test
    fun `when entity has null monthlyTopUp then domain also has null`() = runTest {
        val userId = 1
        val e = entity(id = 4, monthlyTopUp = null)
        every { dao.getByUser(userId) } returns flowOf(listOf(e))

        val domain = repository.getByUser(userId).first().first()

        assertNull(domain.monthlyTopUp)
    }

    @Test
    fun `when getByUser called for two different users then each gets own list`() = runTest {
        every { dao.getByUser(1) } returns flowOf(listOf(entity(id = 1, userId = 1)))
        every { dao.getByUser(2) } returns flowOf(listOf(entity(id = 2, userId = 2), entity(id = 3, userId = 2)))

        val user1 = repository.getByUser(1).first()
        val user2 = repository.getByUser(2).first()

        assertEquals(1, user1.size)
        assertEquals(2, user2.size)
    }

    @Test
    fun `when save called then dao insert is invoked exactly once`() = runTest {
        val e = entity(id = 1)
        coEvery { dao.insert(e) } just Runs

        repository.save(e)

        coVerify(exactly = 1) { dao.insert(e) }
    }

    @Test
    fun `when save called multiple times then dao insert is invoked same number of times`() = runTest {
        val e1 = entity(id = 1)
        val e2 = entity(id = 2)
        coEvery { dao.insert(any()) } just Runs

        repository.save(e1)
        repository.save(e2)

        coVerify(exactly = 2) { dao.insert(any()) }
    }

    @Test
    fun `when delete called then dao delete is invoked with correct entity`() = runTest {
        val e = entity(id = 5)
        coEvery { dao.delete(e) } just Runs

        repository.delete(e)

        coVerify(exactly = 1) { dao.delete(e) }
    }

    @Test
    fun `when delete called then save is not triggered`() = runTest {
        val e = entity(id = 7)
        coEvery { dao.delete(e) } just Runs

        repository.delete(e)

        coVerify(exactly = 0) { dao.insert(any()) }
    }
}