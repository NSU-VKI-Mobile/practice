package ci.nsu.mobile.auth.ui

import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.model.dto.GroupDto
import ci.nsu.mobile.auth.data.model.dto.UserDto
import ci.nsu.mobile.auth.data.model.request.PersonDto
import ci.nsu.mobile.auth.data.model.request.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.ui.AuthViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @MockK
    private lateinit var repo: AuthRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun registerRequest() = RegisterRequest(
        login = "user",
        password = "pass",
        email = "user@mail.com",
        phoneNumber = "79001234567",
        roleId = 1,
        authAllowed = true,
        person = PersonDto("Ivan", "Ivanov", "Ivanovich", "2000-01-01", "M", 1)
    )

    @Test
    fun `when ViewModel initialized then all fields have default values`() {
        assertTrue(viewModel.users.isEmpty())
        assertTrue(viewModel.groups.isEmpty())
        assertFalse(viewModel.isLoading)
        assertNull(viewModel.error)
        assertEquals("", viewModel.lastLogin)
        assertEquals("", viewModel.lastPassword)
        assertNull(viewModel.qrLogin)
        assertNull(viewModel.qrPassword)
    }

    @Test
    fun `when login succeeds then onSuccess callback is invoked`() = runTest(testDispatcher) {
        coEvery { repo.login("user", "pass") } returns Result.success(Unit)
        var called = false

        viewModel.login("user", "pass") { called = true }
        advanceUntilIdle()

        assertTrue(called)
    }

    @Test
    fun `when login succeeds then lastLogin and lastPassword are saved`() = runTest(testDispatcher) {
        coEvery { repo.login("user", "pass") } returns Result.success(Unit)

        viewModel.login("user", "pass") {}
        advanceUntilIdle()

        assertEquals("user", viewModel.lastLogin)
        assertEquals("pass", viewModel.lastPassword)
    }

    @Test
    fun `when login succeeds then error stays null`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.success(Unit)

        viewModel.login("user", "pass") {}
        advanceUntilIdle()

        assertNull(viewModel.error)
    }

    @Test
    fun `when login fails then onSuccess is not called`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.failure(RuntimeException("Unauthorized"))
        var called = false

        viewModel.login("user", "wrong") { called = true }
        advanceUntilIdle()

        assertFalse(called)
    }

    @Test
    fun `when login fails then error message is set`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.failure(RuntimeException("Unauthorized"))

        viewModel.login("user", "wrong") {}
        advanceUntilIdle()

        assertEquals("Unauthorized", viewModel.error)
    }

    @Test
    fun `when login fails then credentials are not saved`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.failure(RuntimeException("err"))

        viewModel.login("user", "wrong") {}
        advanceUntilIdle()

        assertEquals("", viewModel.lastLogin)
        assertEquals("", viewModel.lastPassword)
    }

    @Test
    fun `when login completes then isLoading resets to false`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.success(Unit)

        viewModel.login("user", "pass") {}
        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `when login fails then isLoading resets to false`() = runTest(testDispatcher) {
        coEvery { repo.login(any(), any()) } returns Result.failure(RuntimeException("err"))

        viewModel.login("user", "pass") {}
        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `when register succeeds then onSuccess is called`() = runTest(testDispatcher) {
        val req = registerRequest()
        coEvery { repo.register(req) } returns Result.success(Unit)
        var called = false

        viewModel.register(req) { called = true }
        advanceUntilIdle()

        assertTrue(called)
        assertNull(viewModel.error)
    }

    @Test
    fun `when register fails then error is set and onSuccess not called`() = runTest(testDispatcher) {
        coEvery { repo.register(any()) } returns Result.failure(RuntimeException("Conflict"))
        var called = false

        viewModel.register(registerRequest()) { called = true }
        advanceUntilIdle()

        assertFalse(called)
        assertEquals("Conflict", viewModel.error)
    }

    @Test
    fun `when register completes then isLoading resets to false`() = runTest(testDispatcher) {
        coEvery { repo.register(any()) } returns Result.success(Unit)

        viewModel.register(registerRequest()) {}
        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `when loadUsers succeeds then users list is populated`() = runTest(testDispatcher) {
        val users = listOf(UserDto(1, "alice"), UserDto(2, "bob"))
        coEvery { repo.getUsers() } returns Result.success(users)

        viewModel.loadUsers()
        advanceUntilIdle()

        assertEquals(2, viewModel.users.size)
        assertEquals("alice", viewModel.users.first().login)
    }

    @Test
    fun `when loadUsers succeeds then isLoading resets to false`() = runTest(testDispatcher) {
        coEvery { repo.getUsers() } returns Result.success(emptyList())

        viewModel.loadUsers()
        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `when loadUsers fails then error is set and users list stays empty`() = runTest(testDispatcher) {
        coEvery { repo.getUsers() } returns Result.failure(RuntimeException("Not found"))

        viewModel.loadUsers()
        advanceUntilIdle()

        assertEquals("Not found", viewModel.error)
        assertTrue(viewModel.users.isEmpty())
    }

    @Test
    fun `when loadGroups succeeds then groups list is populated`() = runTest(testDispatcher) {
        val groups = listOf(GroupDto(1, "G1"), GroupDto(2, "G2"))
        coEvery { repo.getGroups() } returns Result.success(groups)

        viewModel.loadGroups()
        advanceUntilIdle()

        assertEquals(2, viewModel.groups.size)
    }

    @Test
    fun `when loadGroups fails then error is set`() = runTest(testDispatcher) {
        coEvery { repo.getGroups() } returns Result.failure(RuntimeException("Timeout"))

        viewModel.loadGroups()
        advanceUntilIdle()

        assertEquals("Timeout", viewModel.error)
        assertTrue(viewModel.groups.isEmpty())
    }

    @Test
    fun `when applyQrCredentials called then qrLogin and qrPassword are set`() {
        viewModel.applyQrCredentials("qruser", "qrpass")

        assertEquals("qruser", viewModel.qrLogin)
        assertEquals("qrpass", viewModel.qrPassword)
    }

    @Test
    fun `when clearQrData called then qrLogin and qrPassword become null`() {
        viewModel.applyQrCredentials("qruser", "qrpass")

        viewModel.clearQrData()

        assertNull(viewModel.qrLogin)
        assertNull(viewModel.qrPassword)
    }

    @Test
    fun `when logout called then TokenManager clear is invoked`() {
        mockkObject(TokenManager)
        every { TokenManager.clear() } just Runs

        viewModel.logout()

        verify(exactly = 1) { TokenManager.clear() }
        unmockkObject(TokenManager)
    }
}