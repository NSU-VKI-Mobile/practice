package ci.nsu.mobile.calculations.data.ui

import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.entity.DepositEntity
import ci.nsu.mobile.domain.model.DepositCalculation
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DepositViewModelTest {

    @MockK
    private lateinit var dao: DepositDao

    private val testDispatcher = UnconfinedTestDispatcher()
    private var currentUserId = 1

    private lateinit var viewModel: DepositViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        currentUserId = 1
        every { dao.getByUser(any()) } returns flowOf(emptyList())
        viewModel = DepositViewModel(dao) { currentUserId }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun prepareValidInput(
        start: String = "10000",
        months: String = "12",
        rate: Double = 10.0,
        topUp: String = "0"
    ) {
        viewModel.startAmount = start
        viewModel.months = months
        viewModel.rate = rate
        viewModel.monthlyTopUp = topUp
    }

    private fun entity(id: Int = 1, userId: Int = 1) = DepositEntity(
        id = id, userId = userId, startAmount = 1000.0, months = 3,
        rate = 5.0, monthlyTopUp = null, finalAmount = 1150.0,
        profit = 150.0, date = 0L
    )

    @Test
    fun `when ViewModel initialized then all fields have default empty or zero values`() {
        assertEquals("", viewModel.startAmount)
        assertEquals("", viewModel.months)
        assertEquals(0.0, viewModel.rate)
        assertEquals("", viewModel.monthlyTopUp)
        assertEquals(0, viewModel.currentStep)
        assertNull(viewModel.result)
        assertNull(viewModel.savedMessage)
    }

    @Test
    fun `when ViewModel initialized then history emits empty list initially`() {
        assertEquals(emptyList(), viewModel.history.value)
    }

    @Test
    fun `when valid input with no top-up then final amount equals compound interest`() {
        // 10000 × 1.10^1 = 11000
        prepareValidInput(start = "10000", months = "1", rate = 10.0, topUp = "0")

        viewModel.calculateDeposit()

        val r = viewModel.result
        assertNotNull(r)
        assertEquals(11_000.0, r.finalAmount, 0.01)
        assertEquals(1_000.0, r.profit, 0.01)
    }

    @Test
    fun `when valid input with monthly top-up then top-up is included before each interest cycle`() {
        // (1000+500)*1.10 = 1650
        prepareValidInput(start = "1000", months = "1", rate = 10.0, topUp = "500")

        viewModel.calculateDeposit()

        val r = viewModel.result
        assertNotNull(r)
        assertEquals(1_650.0, r.finalAmount, 0.01)
    }

    @Test
    fun `when calculateDeposit succeeds then currentStep advances to 2`() {
        prepareValidInput()

        viewModel.calculateDeposit()

        assertEquals(2, viewModel.currentStep)
    }

    @Test
    fun `when calculateDeposit called then result carries current userId`() {
        currentUserId = 42
        viewModel.reloadForUser()
        prepareValidInput()

        viewModel.calculateDeposit()

        assertEquals(42, viewModel.result?.userId)
    }

    @Test
    fun `when monthlyTopUp is empty string then it defaults to zero in calculation`() {
        viewModel.startAmount = "1000"
        viewModel.months = "1"
        viewModel.rate = 10.0
        viewModel.monthlyTopUp = ""   // пусто → toDoubleOrNull() = null → базово до 0.0

        viewModel.calculateDeposit()

        assertEquals(1_100.0, viewModel.result?.finalAmount ?: 0.0, 0.01)
    }

    @Test
    fun `when startAmount is non-numeric then calculateDeposit does nothing`() {
        viewModel.startAmount = "abc"
        viewModel.months = "12"
        viewModel.rate = 10.0

        viewModel.calculateDeposit()

        assertNull(viewModel.result)
        assertEquals(0, viewModel.currentStep)
    }

    @Test
    fun `when startAmount is empty then calculateDeposit does nothing`() {
        viewModel.startAmount = ""
        viewModel.months = "12"
        viewModel.rate = 10.0

        viewModel.calculateDeposit()

        assertNull(viewModel.result)
    }

    @Test
    fun `when months is non-numeric then calculateDeposit does nothing`() {
        viewModel.startAmount = "10000"
        viewModel.months = "twelve"
        viewModel.rate = 10.0

        viewModel.calculateDeposit()

        assertNull(viewModel.result)
        assertEquals(0, viewModel.currentStep)
    }

    @Test
    fun `when months is empty then calculateDeposit does nothing`() {
        viewModel.startAmount = "10000"
        viewModel.months = ""
        viewModel.rate = 10.0

        viewModel.calculateDeposit()

        assertNull(viewModel.result)
    }

    @Test
    fun `when both fields are invalid then calculateDeposit does nothing`() {
        viewModel.calculateDeposit()

        assertNull(viewModel.result)
        assertEquals(0, viewModel.currentStep)
    }

    @Test
    fun `when saveDeposit called after calculation then dao insert is invoked`() = runTest(testDispatcher) {
        prepareValidInput()
        viewModel.calculateDeposit()
        coEvery { dao.insert(any()) } just Runs

        viewModel.saveDeposit()
        advanceUntilIdle()

        coVerify(exactly = 1) { dao.insert(any()) }
    }

    @Test
    fun `when saveDeposit called after calculation then savedMessage is set`() = runTest(testDispatcher) {
        prepareValidInput()
        viewModel.calculateDeposit()
        coEvery { dao.insert(any()) } just Runs

        viewModel.saveDeposit()
        advanceUntilIdle()

        assertEquals("Расчет сохранен", viewModel.savedMessage)
    }

    @Test
    fun `when saveDeposit called without prior calculation then dao is not invoked`() = runTest(testDispatcher) {
        coEvery { dao.insert(any()) } just Runs

        viewModel.saveDeposit()
        advanceUntilIdle()

        coVerify(exactly = 0) { dao.insert(any()) }
        assertNull(viewModel.savedMessage)
    }

    @Test
    fun `when saveDeposit called then saved entity contains correct userId`() = runTest(testDispatcher) {
        currentUserId = 7
        viewModel.reloadForUser()
        prepareValidInput()
        viewModel.calculateDeposit()
        val captured = slot<DepositEntity>()
        coEvery { dao.insert(capture(captured)) } just Runs

        viewModel.saveDeposit()
        advanceUntilIdle()

        assertEquals(7, captured.captured.userId)
    }

    @Test
    fun `when deleteDeposit called then dao deleteById is invoked with correct id`() = runTest(testDispatcher) {
        val calc = DepositCalculation(
            id = 99, userId = 1, startAmount = 1000.0, months = 3,
            rate = 5.0, monthlyTopUp = null, finalAmount = 1150.0,
            profit = 150.0, date = 0L
        )
        coEvery { dao.deleteById(99) } just Runs

        viewModel.deleteDeposit(calc)
        advanceUntilIdle()

        coVerify(exactly = 1) { dao.deleteById(99) }
    }

    @Test
    fun `when resetCalculation called then all mutable state is cleared`() {
        prepareValidInput(start = "5000", months = "6", rate = 8.0, topUp = "200")
        viewModel.calculateDeposit()
        assertTrue(viewModel.result != null)

        viewModel.resetCalculation()

        assertEquals("", viewModel.startAmount)
        assertEquals("", viewModel.months)
        assertEquals(0.0, viewModel.rate)
        assertEquals("", viewModel.monthlyTopUp)
        assertNull(viewModel.result)
        assertEquals(0, viewModel.currentStep)
        assertNull(viewModel.savedMessage)
    }

    @Test
    fun `when goToStep called then currentStep is updated to given value`() {
        viewModel.goToStep(1)
        assertEquals(1, viewModel.currentStep)
    }

    @Test
    fun `when goToStep called multiple times then last value wins`() {
        viewModel.goToStep(1)
        viewModel.goToStep(0)
        assertEquals(0, viewModel.currentStep)
    }

    @Test
    fun `when months is less than 6 then resolveRate returns 15`() {
        viewModel.months = "5"
        assertEquals(listOf(15.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is exactly 1 then resolveRate returns 15 (boundary)`() {
        viewModel.months = "1"
        assertEquals(listOf(15.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is 6 then resolveRate returns 10`() {
        viewModel.months = "6"
        assertEquals(listOf(10.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is 11 then resolveRate returns 10 (upper boundary of mid-tier)`() {
        viewModel.months = "11"
        assertEquals(listOf(10.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is exactly 12 then resolveRate returns 5`() {
        viewModel.months = "12"
        assertEquals(listOf(5.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is more than 12 then resolveRate returns 5`() {
        viewModel.months = "24"
        assertEquals(listOf(5.0), viewModel.resolveRate())
    }

    @Test
    fun `when months is invalid then resolveRate returns empty list`() {
        viewModel.months = "invalid"
        assertTrue(viewModel.resolveRate().isEmpty())
    }

    @Test
    fun `when months is empty then resolveRate returns empty list`() {
        viewModel.months = ""
        assertTrue(viewModel.resolveRate().isEmpty())
    }

    @Test
    fun `when userId is 0 (guest) then history is empty regardless of dao`() {
        currentUserId = 0
        every { dao.getByUser(any()) } returns flowOf(listOf(entity()))
        viewModel = DepositViewModel(dao) { currentUserId }

        assertEquals(emptyList(), viewModel.history.value)
    }

    @Test
    fun `when reloadForUser called with new userId then history updates`() = runTest(testDispatcher) {
        currentUserId = 3
        every { dao.getByUser(3) } returns flowOf(listOf(entity(id = 10, userId = 3)))

        val job = launch { viewModel.history.collect {} }

        viewModel.reloadForUser()
        advanceUntilIdle()

        assertEquals(1, viewModel.history.value.size)
        assertEquals(10, viewModel.history.value.first().id)

        job.cancel()
    }
}