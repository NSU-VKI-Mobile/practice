package ci.nsu.mobile.auth.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ci.nsu.mobile.auth.data.dto.AuthResponse
import ci.nsu.mobile.auth.data.repository.AuthRepository
import io.mockk.coEvery // Импортируем функцию для обучения мока
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreen_DisplaysCorrectly_And_AcceptsInput() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val navController = TestNavHostController(context)

        // 1. Создаем мок
        val mockRepository = mockk<AuthRepository>()

        // 2. УЧИМ МОК: Когда кто-то вызывает функцию логина с любыми параметрами (any()),
        // возвращаем фейковый, но правильный объект AuthResponse
        coEvery { mockRepository.login(any(), any()) } returns Result.success(
            AuthResponse(token = "12345") // твои фейковые данные
        )

        coEvery { mockRepository.getUsers() } returns Result.success(emptyList())

        // 3. Передаем обученный мок во ViewModel
        val viewModel = AuthViewModel(repository = mockRepository)

        composeTestRule.setContent {
            LoginScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Вход в систему").assertExists()
        composeTestRule.onNodeWithText("Логин").performTextInput("test_user")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")
        composeTestRule.onNodeWithText("Войти").performClick()
    }
}