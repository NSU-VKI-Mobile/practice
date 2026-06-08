package ci.nsu.mobile.auth.navigation

import UsersScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.QrGenerateScreen
import ci.nsu.mobile.auth.ui.screens.QrScanScreen
import ci.nsu.mobile.auth.ui.screens.RegistrationScreen
import ci.nsu.mobile.auth.ui.screens.UserOwnScreen
import ci.nsu.mobile.auth.viewModels.code.CodeViewModel
import ci.nsu.mobile.auth.viewModels.login.LoginViewModel
import ci.nsu.mobile.auth.viewModels.registration.RegistrationViewModel
import ci.nsu.mobile.auth.viewModels.userOwn.UserOwnViewModel
import ci.nsu.mobile.auth.viewModels.users.UsersViewModel
import ci.nsu.mobile.domain.navigation.Screens

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegistrationViewModel,
    usersViewModel: UsersViewModel,
    userOwnViewModel: UserOwnViewModel,
    qrCodeViewModel: CodeViewModel,
    onNavigateToHistory: () -> Unit
) {
    composable(Screens.LoginScreen.route) {
        val savedLogin = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_login") ?: ""
        val savedPassword = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_password") ?: ""

        LoginScreen(
            onLoginSuccess = onNavigateToHistory,
            navTo = { navigateTo -> navController.navigate(navigateTo) },
            openQRScreen = {
                navController.navigate(Screens.QRScanScreen.route)
            },
            viewModel = loginViewModel,
            qrLogin = savedLogin,
            qrPassword = savedPassword
        )

        LaunchedEffect(Unit) {
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("qr_login")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("qr_password")
        }
    }

    composable(Screens.RegistrationScreen.route) {
        RegistrationScreen(
            viewModel = registerViewModel,
            onRegisterSuccess = {
                navController.navigate(Screens.LoginScreen.route) {
                    popUpTo(Screens.RegistrationScreen.route) { inclusive = true }
                }
            }
        )
    }

    composable(Screens.UsersScreen.route) {
        UsersScreen(
            viewModel = usersViewModel
        )
    }

    composable(Screens.UserOwnScreen.route) {
        UserOwnScreen(
            viewModel = userOwnViewModel,
            openGenerateQR = {
                navController.navigate(
                    Screens.QRGenerateScreen.route
                )
            }
        )
    }

    composable(Screens.QRGenerateScreen.route) {
        QrGenerateScreen(
            viewModel = qrCodeViewModel,
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screens.QRScanScreen.route) {
        QrScanScreen(
            viewModel = qrCodeViewModel,
            onBackClick = { login, password ->
                if (login.isNotEmpty() && password.isNotEmpty()) {
                    // Сохраняем данные и возвращаемся на экран логина
                    navController.previousBackStackEntry?.savedStateHandle?.set("qr_login", login)
                    navController.previousBackStackEntry?.savedStateHandle?.set("qr_password", password)
                }
                navController.popBackStack()
            }
        )
    }
}