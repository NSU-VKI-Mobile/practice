package com.example.calculations.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calculations.data.dba.Deposit
import com.example.calculations.data.dba.DepositDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class DepositRepositoryTest {

    private lateinit var mockDao: DepositDao
    private lateinit var repository: DepositRepository

    @Before
    fun setUp() {
        mockDao = mock()
        repository = DepositRepository(mockDao)
    }

    @After
    fun tearDown() {
        // No additional cleanup needed
    }

    // 1. Тест добавления нового расчёта
    @Test
    fun `when addDeposit called then dao insert called once`() = runTest {
        // Given
        val deposit = Deposit(
            userId = 1L,
            initialAmount = 1000.0,
            periodMonths = 12,
            interestRate = 5.0,
            monthlyTopUp = null,
            finalAmount = 1050.0,
            interestEarned = 50.0,
            calculationDate = System.currentTimeMillis()
        )

        // When
        repository.addDeposit(deposit)

        // Then
        verify(mockDao, timeout(1000)).addDeposit(deposit)
    }

    // 2. Тест получения расчётов пользователя (Flow)
    @Test
    fun `when getDepositUser called then returns flow from dao`() = runTest {
        // Given
        val userId = 1L
        val expectedDeposits = listOf(
            Deposit(id = 1, userId = userId, initialAmount = 100.0, periodMonths = 6, interestRate = 5.0, monthlyTopUp = null, finalAmount = 102.5, interestEarned = 2.5, calculationDate = 123L)
        )
        whenever(mockDao.getDepositsUser(userId)).thenReturn(flowOf(expectedDeposits))

        // When
        val result = repository.getDepositUser(userId).first()

        // Then
        assert(result == expectedDeposits)
        verify(mockDao).getDepositsUser(userId)
    }

    // 3. Тест получения истории расчётов (LiveData)
    @Test
    fun `when depositList property accessed then returns live data from dao`() {
        // Given
        val liveData = MutableLiveData<List<Deposit>>()
        whenever(mockDao.getDeposits()).thenReturn(liveData)

        // When
        val result = repository.depositList

        // Then
        assert(result == liveData)
        verify(mockDao).getDeposits()
    }

    // 4. Тест удаления расчёта
    @Test
    fun `when deleteDeposit called then dao deleteById called`() = runTest {
        // Given
        val calculationId = 42L

        // When
        repository.deleteDeposit(calculationId)

        // Then
        verify(mockDao, timeout(1000)).deleteById(calculationId)
    }

    // 5. Тест обновления существующего расчёта (реализуется через addDeposit с существующим id)
    @Test
    fun `when update existing deposit then dao insert called with same id`() = runTest {
        // Given
        val existingDeposit = Deposit(
            id = 5L,
            userId = 1L,
            initialAmount = 200.0,
            periodMonths = 12,
            interestRate = 10.0,
            monthlyTopUp = 50.0,
            finalAmount = 300.0,
            interestEarned = 100.0,
            calculationDate = 456L
        )

        // When
        repository.addDeposit(existingDeposit)

        // Then
        verify(mockDao).addDeposit(existingDeposit)
    }

    // 6. Тест валидации данных перед сохранением (на уровне репозитория нет валидации,
    // но можно проверить, что репозиторий не фильтрует некорректные данные)
    @Test
    fun `when deposit with negative amount added then still passed to dao`() = runTest {
        // Given
        val invalidDeposit = Deposit(
            userId = 1L,
            initialAmount = -100.0, // отрицательная сумма
            periodMonths = 12,
            interestRate = 5.0,
            monthlyTopUp = null,
            finalAmount = -95.0,
            interestEarned = 5.0,
            calculationDate = System.currentTimeMillis()
        )

        // When
        repository.addDeposit(invalidDeposit)

        // Then
        verify(mockDao).addDeposit(invalidDeposit)
    }
}