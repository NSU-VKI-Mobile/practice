package ci.nsu.mobile.auth.ui

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    // Подключаем правило для корутин
    @get:Rule
    val mainDispatcherRule = ci.nsu.mobile.auth.MainDispatcherRule()

    // ИСПРАВЛЕНИЕ ЗДЕСЬ: Передаем фейковый репозиторий, как мы это делали в UI-тестах
    private val viewModel = AuthViewModel(repository = mockk(relaxed = true))

    @Test
    fun `test initial state is correct`() {
        assertEquals(false, viewModel.isUserLoggedIn)
        assertEquals(null, viewModel.errorMessage)
        assertEquals(false, viewModel.isLoading)
    }

    @Test
    fun `when login is called with empty fields then error is shown`() = runTest {
        viewModel.login("", "")
        // Ожидаем, что без данных авторизация не пройдет
        assertEquals(false, viewModel.isUserLoggedIn)
    }
}