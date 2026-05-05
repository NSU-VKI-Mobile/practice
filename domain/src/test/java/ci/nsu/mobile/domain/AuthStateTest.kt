package ci.nsu.mobile.domain

import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthStateTest {
    @Test
    fun authenticatedStateKeepsUserContract() {
        val user = User(id = 42L, login = "student", email = "student@example.com")
        val state = AuthState.Authenticated(user)

        assertEquals(user, state.user)
        assertEquals(AuthState.Authenticated(user), state)
    }
}
