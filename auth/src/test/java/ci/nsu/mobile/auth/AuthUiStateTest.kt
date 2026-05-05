package ci.nsu.mobile.auth

import ci.nsu.mobile.auth.viewmodel.AuthUiState
import ci.nsu.mobile.auth.viewmodel.RegisterUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUiStateTest {
    @Test
    fun defaultAuthStateIsAnonymousAndEmpty() {
        val state = AuthUiState()

        assertFalse(state.isLoading)
        assertFalse(state.isAuthenticated)
        assertTrue(state.users.isEmpty())
        assertTrue(state.groups.isEmpty())
    }

    @Test
    fun defaultRegisterStateHasNoGroupsOrError() {
        val state = RegisterUiState()

        assertFalse(state.isLoading)
        assertTrue(state.groups.isEmpty())
        assertTrue(state.error == null)
    }
}
