package com.example.calculations.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.calculations.data.dba.Deposit
import com.example.calculations.data.repository.DepositRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.math.pow

class DepositsViewModelTest {

    private lateinit var application: Application
    private lateinit var repository: DepositRepository
    private lateinit var viewModel: DepositsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        application = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        every { repository.depositList } returns MutableLiveData(null)
        every { repository.getDepositUser(any()) } returns flowOf(emptyList())
        viewModel = DepositsViewModel(application, repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initialization uiState has default values`() {
        // Given

        // When

        // Then
        val state = viewModel.uiState.value
        assertEquals("", state.initialAmount)
        assertEquals("", state.periodMonths)
        assertEquals("", state.interestRate)
        assertEquals("", state.monthlyTopUp)
        assertEquals(0.0, state.finalAmount, 0.01)
        assertEquals(0.0, state.interestEarned, 0.01)
        assertTrue(state.availableInterestRate.isEmpty())
        assertFalse(state.isInitialAmountValid)
        assertFalse(state.isPeriodMonthsValid)
        assertFalse(state.isInterestRateValid)
        assertTrue(state.isMonthlyTopUpValid)
        assertFalse(state.isAllCorrect)
    }

    @Test
    fun `setInitialAmount updates state correctly`() {
        // Given
        val inputAmount = "1000"

        // When
        viewModel.setInitialAmount(inputAmount)

        // Then
        assertEquals(inputAmount, viewModel.uiState.value.initialAmount)
    }

    @Test
    fun `setPeriodMonths updates period and available interest rates, resets interestRate`() {
        // Given
        val period = "12"

        // When
        viewModel.setPeriodMonths(period)

        // Then
        val state = viewModel.uiState.value
        assertEquals(period, state.periodMonths)
        assertEquals(listOf(0.15, 0.10, 0.05), state.availableInterestRate)
        assertEquals("", state.interestRate)
    }

    @Test
    fun `setInterestRate updates interestRate`() {
        // Given
        val rate = "0.12"

        // When
        viewModel.setInterestRate(rate)

        // Then
        assertEquals(rate, viewModel.uiState.value.interestRate)
    }

    @Test
    fun `setMonthlyTopUp updates monthlyTopUp`() {
        // Given
        val topUp = "500"

        // When
        viewModel.setMonthlyTopUp(topUp)

        // Then
        assertEquals(topUp, viewModel.uiState.value.monthlyTopUp)
    }

    @Test
    fun `validation flags work correctly`() {
        // Given

        // When
        viewModel.setInitialAmount("1000")
        viewModel.setPeriodMonths("6")
        viewModel.setInterestRate("0.08")
        viewModel.setMonthlyTopUp("")

        // Then
        assertTrue(viewModel.uiState.value.isInitialAmountValid)
        assertTrue(viewModel.uiState.value.isPeriodMonthsValid)
        assertTrue(viewModel.uiState.value.isInterestRateValid)
        assertTrue(viewModel.uiState.value.isMonthlyTopUpValid)
        assertTrue(viewModel.uiState.value.isAllCorrect)
    }

    @Test
    fun `validation rejects negative or invalid inputs`() {
        // Given

        // When
        viewModel.setInitialAmount("abc")
        viewModel.setPeriodMonths("xyz")
        viewModel.setInterestRate("def")
        viewModel.setMonthlyTopUp("abc")

        // Then
        assertFalse(viewModel.uiState.value.isInitialAmountValid)
        assertFalse(viewModel.uiState.value.isPeriodMonthsValid)
        assertFalse(viewModel.uiState.value.isInterestRateValid)
        assertFalse(viewModel.uiState.value.isMonthlyTopUpValid)
        assertFalse(viewModel.uiState.value.isAllCorrect)
    }

    @Test
    fun `calcFinalAmountAndEarned computes final amount correctly without monthly top-up`() {
        // Given
        viewModel.setInitialAmount("1000")
        viewModel.setPeriodMonths("12")
        viewModel.setInterestRate("0.12")
        viewModel.setMonthlyTopUp("")

        // When
        viewModel.calcFinalAmountAndEarned()

        // Then
        val state = viewModel.uiState.value
        val expected = 1000 * (1 + 0.12 / 12).pow(12)
        assertEquals(expected, state.finalAmount, 0.001)
    }

    @Test
    fun `calcFinalAmountAndEarned computes final amount correctly with monthly top-up`() {
        // Given
        viewModel.setInitialAmount("1000")
        viewModel.setPeriodMonths("6")
        viewModel.setInterestRate("0.12")
        viewModel.setMonthlyTopUp("100")

        // When
        viewModel.calcFinalAmountAndEarned()

        // Then
        val state = viewModel.uiState.value
        val monthlyRate = 0.12 / 12
        val expected = 1000 * (1 + monthlyRate).pow(6) +
                100 * (((1 + monthlyRate).pow(6) - 1) / monthlyRate)
        assertEquals(expected, state.finalAmount, 0.001)
    }

    @Test
    fun `calcFinalAmountAndEarned computes interestEarned correctly without top-up`() {
        // Given
        viewModel.setInitialAmount("1000")
        viewModel.setPeriodMonths("12")
        viewModel.setInterestRate("0.12")
        viewModel.setMonthlyTopUp("")

        // When
        viewModel.calcFinalAmountAndEarned()

        // Then
        val state = viewModel.uiState.value
        val finalAmount = 1000 * (1 + 0.12 / 12).pow(12)
        val expectedEarned = (finalAmount - 1000) / 1000
        assertEquals(expectedEarned, state.interestEarned, 0.001)
    }

    @Test
    fun `calcFinalAmountAndEarned computes interestEarned correctly with top-up`() {
        // Given
        viewModel.setInitialAmount("1000")
        viewModel.setPeriodMonths("6")
        viewModel.setInterestRate("0.12")
        viewModel.setMonthlyTopUp("100")

        // When
        viewModel.calcFinalAmountAndEarned()

        // Then
        val state = viewModel.uiState.value
        val monthlyRate = 0.12 / 12
        val finalAmount = 1000 * (1 + monthlyRate).pow(6) +
                100 * (((1 + monthlyRate).pow(6) - 1) / monthlyRate)
        val expectedEarned = (finalAmount - 1000 - 100 * 6) / 1000
        assertEquals(expectedEarned, state.interestEarned, 0.001)
    }

    @Test
    fun `saveDeposit adds deposit to repository and resets state when valid`() = runTest {
        // Given
        every { repository.addDeposit(any()) } just Runs

        viewModel.setInitialAmount("2000")
        viewModel.setPeriodMonths("3")
        viewModel.setInterestRate("0.10")
        viewModel.setMonthlyTopUp("")
        viewModel.calcFinalAmountAndEarned()
        val beforeReset = viewModel.uiState.value
        assertTrue(beforeReset.isAllCorrect)

        // When
        viewModel.saveDeposit(42L)

        // Then
        val savedSlot = slot<Deposit>()
        verify(exactly = 1) { repository.addDeposit(capture(savedSlot)) }
        val saved = savedSlot.captured
        assertEquals(2000.0, saved.initialAmount, 0.01)
        assertEquals(3, saved.periodMonths)
        assertEquals(0.10, saved.interestRate, 0.01)
        assertEquals(null, saved.monthlyTopUp)
        assertEquals(beforeReset.finalAmount, saved.finalAmount, 0.01)
        assertEquals(beforeReset.interestEarned, saved.interestEarned, 0.01)
        assertEquals(42L, saved.userId)

        val afterReset = viewModel.uiState.value
        assertEquals("", afterReset.initialAmount)
        assertEquals("", afterReset.periodMonths)
        assertEquals("", afterReset.interestRate)
        assertEquals("", afterReset.monthlyTopUp)
        assertEquals(0.0, afterReset.finalAmount, 0.01)
        assertEquals(0.0, afterReset.interestEarned, 0.01)
    }

    @Test
    fun `saveDeposit does nothing when state invalid`() {
        // Given
        viewModel.setInitialAmount("")

        // When
        viewModel.saveDeposit(1L)

        // Then
        verify(inverse = true) { repository.addDeposit(any()) }
    }

    @Test
    fun `setPeriodMonths resets interestRate and updates available rates`() {
        // Given
        viewModel.setPeriodMonths("12")
        viewModel.setInterestRate("0.05")
        assertEquals("0.05", viewModel.uiState.value.interestRate)

        // When
        viewModel.setPeriodMonths("6")

        // Then
        val state = viewModel.uiState.value
        assertEquals("6", state.periodMonths)
        assertEquals(listOf(0.15, 0.10), state.availableInterestRate)
        assertEquals("", state.interestRate)
    }
}