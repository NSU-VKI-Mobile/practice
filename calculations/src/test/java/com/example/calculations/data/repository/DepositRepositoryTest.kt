package com.example.calculations.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.calculations.data.dba.Deposit
import com.example.calculations.data.dba.DepositDao
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DepositRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var depositDao: DepositDao
    private lateinit var repository: DepositRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        depositDao = mockk(relaxed = true)
        repository = DepositRepository(depositDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `addDeposit should call dao addDeposit`() {
        // Given
        val deposit = Deposit(
            userId = 1L,
            initialAmount = 1000.0,
            periodMonths = 12,
            interestRate = 0.05,
            monthlyTopUp = null,
            finalAmount = 1050.0,
            interestEarned = 0.05,
            calculationDate = System.currentTimeMillis()
        )

        // When
        repository.addDeposit(deposit)
        Thread.sleep(100)

        // Then
        verify(exactly = 1) { depositDao.addDeposit(deposit) }
    }

    @Test
    fun `getDepositUser should return flow from dao`() = runTest {
        // Given
        val testUserId = 42L
        val expectedDeposits = listOf(
            Deposit(1, testUserId, 500.0, 6, 0.03, 50.0, 800.0, 0.04, 0L)
        )
        every { depositDao.getDepositsUser(testUserId) } returns flowOf(expectedDeposits)

        // When
        val flow = repository.getDepositUser(testUserId)
        val result = flow.first()

        // Then
        assertEquals(expectedDeposits, result)
        verify(exactly = 1) { depositDao.getDepositsUser(testUserId) }
    }

    @Test
    fun `depositList should return live data from dao`() {
        // Given
        val allDeposits = listOf(
            Deposit(1, 1L, 1000.0, 12, 0.05, null, 1050.0, 0.05, 0L),
            Deposit(2, 2L, 2000.0, 6, 0.04, 100.0, 2200.0, 0.06, 0L)
        )
        val liveData = MutableLiveData(allDeposits)
        every { depositDao.getDeposits() } returns liveData
        val repo = DepositRepository(depositDao)

        // When
        val result = repo.depositList

        // Then
        assertEquals(allDeposits, result.value)
    }

    @Test
    fun `deleteDeposit should call dao deleteById`() = runTest {
        // Given
        val deleteId = 10L
        coEvery { depositDao.deleteById(deleteId) } just Runs

        // When
        repository.deleteDeposit(deleteId)
        Thread.sleep(100)

        // Then
        coVerify(exactly = 1) { depositDao.deleteById(deleteId) }
    }
}