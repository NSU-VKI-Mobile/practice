package ci.nsu.mobile.auth.ui

import android.content.Context
import android.graphics.Bitmap
import ci.nsu.mobile.auth.qr.NotificationHelper
import ci.nsu.mobile.auth.qr.QrGenerator
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class QrViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: QrViewModel
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockkObject(QrGenerator)
        every { QrGenerator.generate(any(), any()) } returns mockk<Bitmap>(relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = QrViewModel()
        mockContext = mockk(relaxed = true)
        mockkObject(NotificationHelper)
        every { NotificationHelper.notifySuccess(any()) } just Runs
        every { NotificationHelper.notifyFailure(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkObject(QrGenerator)
        Dispatchers.resetMain()
        unmockkObject(NotificationHelper)
        clearAllMocks()
    }

    @Test
    fun `when ViewModel initialized then all fields have default values`() {
        assertNull(viewModel.generatedQr)
        assertFalse(viewModel.showQrDialog)
        assertNull(viewModel.savedMessage)
        assertEquals(30, viewModel.timerSeconds)
        assertFalse(viewModel.scanFinished)
        assertNull(viewModel.scannedLogin)
        assertNull(viewModel.scannedPassword)
    }

    @Test
    fun `when generateQr called then generatedQr bitmap is not null`() {
        viewModel.generateQr("user", "pass")

        assertNotNull(viewModel.generatedQr)
    }

    @Test
    fun `when generateQr called then showQrDialog becomes true`() {
        viewModel.generateQr("user", "pass")

        assertTrue(viewModel.showQrDialog)
    }

    @Test
    fun `when generateQr called then savedMessage is reset to null`() {
        viewModel.generateQr("user", "pass")

        assertNull(viewModel.savedMessage)
    }

    @Test
    fun `when dismissQrDialog called then showQrDialog becomes false`() {
        viewModel.generateQr("user", "pass")

        viewModel.dismissQrDialog()

        assertFalse(viewModel.showQrDialog)
    }

    @Test
    fun `when onQrScanned with valid format then scannedLogin and scannedPassword are parsed`() {
        viewModel.onQrScanned(mockContext, "testuser:testpass")

        assertEquals("testuser", viewModel.scannedLogin)
        assertEquals("testpass", viewModel.scannedPassword)
    }

    @Test
    fun `when onQrScanned with valid format then scanFinished becomes true`() {
        viewModel.onQrScanned(mockContext, "testuser:testpass")

        assertTrue(viewModel.scanFinished)
    }

    @Test
    fun `when onQrScanned with valid format then success notification is sent`() {
        viewModel.onQrScanned(mockContext, "testuser:testpass")

        verify(exactly = 1) { NotificationHelper.notifySuccess(mockContext) }
    }

    @Test
    fun `when onQrScanned with password containing colon then full password is preserved`() {
        viewModel.onQrScanned(mockContext, "user:pass:word")

        assertEquals("user", viewModel.scannedLogin)
        assertEquals("pass:word", viewModel.scannedPassword)
    }

    @Test
    fun `when onQrScanned with invalid format then scannedLogin and scannedPassword stay null`() {
        viewModel.onQrScanned(mockContext, "invaliddatanocol")

        assertNull(viewModel.scannedLogin)
        assertNull(viewModel.scannedPassword)
    }

    @Test
    fun `when onQrScanned with invalid format then failure notification is sent`() {
        viewModel.onQrScanned(mockContext, "invaliddatanocol")

        verify(exactly = 1) { NotificationHelper.notifyFailure(mockContext) }
    }

    @Test
    fun `when onQrScanned called twice then second call is ignored`() {
        viewModel.onQrScanned(mockContext, "user1:pass1")
        viewModel.onQrScanned(mockContext, "user2:pass2")

        assertEquals("user1", viewModel.scannedLogin)
        assertEquals("pass1", viewModel.scannedPassword)
    }

    @Test
    fun `when onQrScanned called twice then notification is sent only once`() {
        viewModel.onQrScanned(mockContext, "user1:pass1")
        viewModel.onQrScanned(mockContext, "user2:pass2")

        verify(exactly = 1) { NotificationHelper.notifySuccess(any()) }
    }

    @Test
    fun `when resetScan called then all scan fields are cleared`() {
        viewModel.onQrScanned(mockContext, "user:pass")

        viewModel.resetScan()

        assertEquals(30, viewModel.timerSeconds)
        assertFalse(viewModel.scanFinished)
        assertNull(viewModel.scannedLogin)
        assertNull(viewModel.scannedPassword)
    }

    @Test
    fun `when timer runs to zero then scanFinished becomes true`() = runTest(testDispatcher) {
        viewModel.startTimer()
        advanceTimeBy(30_000)
        advanceUntilIdle()

        assertTrue(viewModel.scanFinished)
    }

    @Test
    fun `when timer runs to zero then timerSeconds is 0`() = runTest(testDispatcher) {
        viewModel.startTimer()
        advanceTimeBy(30_000)
        advanceUntilIdle()

        assertEquals(0, viewModel.timerSeconds)
    }

    @Test
    fun `when stopTimer called then timer stops decrementing`() = runTest(testDispatcher) {
        viewModel.startTimer()
        advanceTimeBy(5_000)
        viewModel.stopTimer()
        val secondsAfterStop = viewModel.timerSeconds

        advanceTimeBy(10_000)

        assertEquals(secondsAfterStop, viewModel.timerSeconds)
    }

    @Test
    fun `when startTimer called again then timerSeconds resets to 30`() = runTest(testDispatcher) {
        viewModel.startTimer()
        advanceTimeBy(10_000)

        viewModel.startTimer()

        assertEquals(30, viewModel.timerSeconds)
    }

    @Test
    fun `when scan succeeds before timer expires then scanFinished is true and timer stops`() =
        runTest(testDispatcher) {
            viewModel.startTimer()
            advanceTimeBy(5_000)

            viewModel.onQrScanned(mockContext, "user:pass")
            advanceTimeBy(30_000)

            assertTrue(viewModel.scanFinished)
            assertEquals("user", viewModel.scannedLogin)
        }
}