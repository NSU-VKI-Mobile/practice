package com.example.auth.vm

import android.app.Application
import android.media.MediaPlayer
import com.example.auth.api.TokenManager
import com.example.auth.api.requestData.AuthTokenRespone
import com.example.auth.data.dto.UserDto
import com.example.auth.data.repository.AuthRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginAndRegViewModelTest {

    private lateinit var application: Application
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: LoginAndRegViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        application = mockk(relaxed = true)
        repository = mockk(relaxed = true)

        mockkObject(TokenManager)
        every { TokenManager.init(any()) } just Runs
        every { TokenManager.token = any() } just Runs
        every { TokenManager.clear() } just Runs
        every { TokenManager.userId = any() } just Runs
        every { TokenManager.login = any() } just Runs
        every { TokenManager.password = any() } just Runs

        coEvery { repository.getGroups() } returns Result.success(emptyList())
        coEvery { repository.getUsers() } returns Result.success(emptyList())

        viewModel = LoginAndRegViewModel(application, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initialization uiState has default values`() {
        // Given

        // When

        //Then
        val state = viewModel.uiState.value
        assertEquals("", state.login)
        assertEquals("", state.password)
        assertEquals("", state.firstName)
        assertEquals("", state.lastName)
        assertEquals("", state.middleName)
        assertNull(state.birthDate)
        assertNull(state.gender)
        assertNull(state.group)
        assertEquals("", state.regLogin)
        assertEquals("", state.regPassword)
        assertEquals("", state.email)
        assertEquals("", state.phone)
        assertFalse(state.isLoading)
    }

    @Test
    fun `setLogin and setPassword update state correctly`() {
        //Given
        var login = "testuser"
        var password = "testpass"

        // When
        viewModel.setLogin(login)
        viewModel.setPassword(password)

        // Then
        assertEquals(login, viewModel.uiState.value.login)
        assertEquals(password, viewModel.uiState.value.password)
    }

    @Test
    fun `validation flags work correctly`() {
        // Given

        // When
        viewModel.setBirthDate("2000-01-01")
        viewModel.setGender("Муж")
        viewModel.setGroup(mockk(relaxed = true))

        // Then
        assertTrue(viewModel.uiState.value.isBirthDateValid)
        assertTrue(viewModel.uiState.value.isGenderValid)
        assertTrue(viewModel.uiState.value.isGroupValid)
        assertTrue(viewModel.uiState.value.isAllCorrect)
    }

    @Test
    fun `validation rejects invalid birthdate`() {
        // Given

        // When
        viewModel.setBirthDate("invalid")

        // Then
        assertFalse(viewModel.uiState.value.isBirthDateValid)
        assertFalse(viewModel.uiState.value.isAllCorrect)
    }

    @Test
    fun `successful login calls onSuccess and clears fields`() = runTest {
        // Given
        val login = "user"
        val password = "pass"
        val fakeToken = "token123"

        viewModel.setLogin(login)
        viewModel.setPassword(password)

        coEvery { repository.login(login, password) } returns Result.success(AuthTokenRespone(fakeToken))
        coEvery { repository.getUsers() } returns Result.success(listOf(UserDto(1, login)))

        // When
        var onSuccessCalled = false
        viewModel.logIn { onSuccessCalled = true }

        // Then
        advanceUntilIdle()
        assertTrue(onSuccessCalled)
        assertEquals(fakeToken, viewModel.token.value)
        assertEquals("", viewModel.uiState.value.login)
        assertEquals("", viewModel.uiState.value.password)
        assertFalse(viewModel.uiState.value.isLoading)

        verify { TokenManager.token = fakeToken }
        verify { TokenManager.userId = 1L }
        verify { TokenManager.login = login }
        verify { TokenManager.password = password }
    }

    @Test
    fun `login error sets errorMessage and no token`() = runTest {
        // Given
        val errorMsg = "Invalid credentials"
        viewModel.setLogin("wrong")
        viewModel.setPassword("wrong")

        coEvery { repository.login(any(), any()) } returns Result.failure(Exception(errorMsg))

        // When
        var onSuccessCalled = false
        viewModel.logIn { onSuccessCalled = true }

        // Then
        advanceUntilIdle()
        assertFalse(onSuccessCalled)
        assertEquals(errorMsg, viewModel.errorMessage)
        assertNull(viewModel.token.value)
        assertFalse(viewModel.uiState.value.isLoading)

        verify(inverse = true) { TokenManager.token = any() }
    }

    @Test
    fun `logout clears token and resets uiState`() = runTest {
        // Given
        coEvery { repository.login(any(), any()) } returns Result.success(AuthTokenRespone("token"))
        coEvery { repository.getUsers() } returns Result.success(emptyList())

        viewModel.setLogin("user")
        viewModel.setPassword("pass")
        viewModel.logIn {}
        advanceUntilIdle()
        assertNotNull(viewModel.token.value)

        // When
        viewModel.logOut()

        // Then
        advanceUntilIdle()
        assertNull(viewModel.token.value)
        assertEquals(LoginAndRegUiState(), viewModel.uiState.value)
        verify { TokenManager.clear() }
    }
}