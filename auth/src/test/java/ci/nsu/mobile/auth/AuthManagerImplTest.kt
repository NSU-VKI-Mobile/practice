package ci.nsu.mobile.auth

import ci.nsu.mobile.domain.model.AuthState
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AuthManagerImplTest {

    private lateinit var manager: AuthManagerImpl

    @Before
    fun setUp() {
        mockkObject(TokenManager)
        manager = AuthManagerImpl()
    }

    @After
    fun tearDown() {
        unmockkObject(TokenManager)
    }

    @Test
    fun `when token is null then isLoggedIn returns false`() {
        every { TokenManager.token } returns null

        assertFalse(manager.isLoggedIn())
    }

    @Test
    fun `when token is set then isLoggedIn returns true`() {
        every { TokenManager.token } returns "some_token"

        assertTrue(manager.isLoggedIn())
    }

    @Test
    fun `when userId is null then getCurrentUser returns null`() {
        every { TokenManager.userId } returns null

        assertNull(manager.getCurrentUser())
    }

    @Test
    fun `when userId is set then getCurrentUser returns User with correct id`() {
        every { TokenManager.userId } returns 42

        val user = manager.getCurrentUser()

        assertNotNull(user)
        assertEquals(42, user.id)
    }

    @Test
    fun `when token is set then observeAuthState emits Authenticated`() = runTest {
        every { TokenManager.token } returns "valid_token"

        val state = manager.observeAuthState().first()

        assertEquals(AuthState.Authenticated, state)
    }

    @Test
    fun `when token is null then observeAuthState emits Unauthenticated`() = runTest {
        every { TokenManager.token } returns null

        val state = manager.observeAuthState().first()

        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `when logout called then TokenManager clear is invoked`() {
        every { TokenManager.clear() } just Runs

        manager.logout()

        verify(exactly = 1) { TokenManager.clear() }
    }
}