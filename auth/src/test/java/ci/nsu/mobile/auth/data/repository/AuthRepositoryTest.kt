package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.api.ApiService
import ci.nsu.mobile.auth.data.model.dto.GroupDto
import ci.nsu.mobile.auth.data.model.dto.UserDto
import ci.nsu.mobile.auth.data.model.request.LoginRequest
import ci.nsu.mobile.auth.data.model.request.PersonDto
import ci.nsu.mobile.auth.data.model.request.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AuthRepositoryTest {

    @MockK
    private lateinit var api: ApiService

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(TokenManager)
        every { TokenManager.token = any() } just Runs
        repository = AuthRepository(api)
    }

    @After
    fun tearDown() {
        unmockkObject(TokenManager)
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
    fun `when login succeeds then result is success`() = runTest {
        coEvery { api.login(LoginRequest("user", "pass")) } returns mapOf("token" to "abc")

        val result = repository.login("user", "pass")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `when login succeeds then token is saved to TokenManager`() = runTest {
        coEvery { api.login(any()) } returns mapOf("token" to "abc123")

        repository.login("user", "pass")

        verify { TokenManager.token = "abc123" }
    }

    @Test
    fun `when login response has no token key then null is saved to TokenManager`() = runTest {
        coEvery { api.login(any()) } returns emptyMap()

        repository.login("user", "pass")

        verify { TokenManager.token = null }
    }

    @Test
    fun `when login api throws then result is failure`() = runTest {
        coEvery { api.login(any()) } throws RuntimeException("Unauthorized")

        val result = repository.login("user", "wrong")

        assertTrue(result.isFailure)
        assertEquals("Unauthorized", result.exceptionOrNull()?.message)
    }

    @Test
    fun `when login api throws then TokenManager token is not set`() = runTest {
        coEvery { api.login(any()) } throws RuntimeException("error")

        repository.login("user", "pass")

        verify(exactly = 0) { TokenManager.token = any() }
    }

    @Test
    fun `when register succeeds then result is success`() = runTest {
        val req = registerRequest()
        coEvery { api.register(req) } just Runs

        val result = repository.register(req)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `when register api throws then result is failure`() = runTest {
        coEvery { api.register(any()) } throws RuntimeException("Conflict")

        val result = repository.register(registerRequest())

        assertTrue(result.isFailure)
        assertEquals("Conflict", result.exceptionOrNull()?.message)
    }

    @Test
    fun `when getUsers succeeds then result contains full user list`() = runTest {
        val users = listOf(UserDto(1, "alice"), UserDto(2, "bob"))
        coEvery { api.getUsers() } returns users

        val result = repository.getUsers()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("alice", result.getOrNull()?.first()?.login)
    }

    @Test
    fun `when getUsers returns empty list then result is success with empty list`() = runTest {
        coEvery { api.getUsers() } returns emptyList()

        val result = repository.getUsers()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun `when getUsers api throws then result is failure`() = runTest {
        coEvery { api.getUsers() } throws RuntimeException("Timeout")

        val result = repository.getUsers()

        assertTrue(result.isFailure)
    }

    @Test
    fun `when getGroups succeeds then result contains full group list`() = runTest {
        val groups = listOf(GroupDto(1, "Group A"), GroupDto(2, "Group B"))
        coEvery { api.getGroups() } returns groups

        val result = repository.getGroups()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Group A", result.getOrNull()?.first()?.groupName)
    }

    @Test
    fun `when getGroups api throws then result is failure`() = runTest {
        coEvery { api.getGroups() } throws RuntimeException("Server error")

        val result = repository.getGroups()

        assertTrue(result.isFailure)
    }
}