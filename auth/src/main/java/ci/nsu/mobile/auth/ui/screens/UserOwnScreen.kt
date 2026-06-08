package ci.nsu.mobile.auth.ui.screens

import UserCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.auth.viewModels.userOwn.UserOwnEvents
import ci.nsu.mobile.auth.viewModels.userOwn.UserOwnViewModel
import ci.nsu.mobile.domain.models.User
import ci.nsu.mobile.ui.components.CustomButton

@Composable
fun UserOwnScreen(
    viewModel: UserOwnViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(20.dp))
                }

                state.errorMessage != null -> {
                    Text(
                        state.errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(10.dp)
                    )
                    if (state.userId != 0) {
                        UserCard(
                            User(
                                userId = state.userId,
                                login = state.userLogin,
                                email = state.userEmail ?: "",
                                personId = state.userPersonId,
                                createdDate = state.userCreatedDate,
                                phoneNumber = state.userPhone,
                                roleId = 1,
                                authAllowed = true,
                                lastLoginDate = state.userLastLoginDate
                            )
                        )
                    }
                }

                else -> {
                    UserCard(
                        User(
                            userId = state.userId,
                            login = state.userLogin,
                            email = state.userEmail ?: "",
                            personId = state.userPersonId,
                            createdDate = state.userCreatedDate,
                            phoneNumber = state.userPhone,
                            roleId = 1,
                            authAllowed = true,
                            lastLoginDate = state.userLastLoginDate
                        )
                    )

                    CustomButton(
                        onClick = { viewModel.onEvent(UserOwnEvents.GetQR) },
                        "Создать QR-код авторизации",
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}