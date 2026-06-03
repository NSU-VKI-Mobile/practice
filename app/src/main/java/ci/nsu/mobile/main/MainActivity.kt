package ci.nsu.mobile.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.AddEditScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.theme.NotifierTheme
import ci.nsu.mobile.main.viewmodel.NotificationViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Разрешение получено или нет
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        setContent {
            NotifierTheme {
                val navController = rememberNavController()
                val viewModel: NotificationViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            val application = application as NotifierApplication
                            NotificationViewModel(
                                repository = application.repository,
                                scheduler = application.notificationScheduler
                            )
                        }
                    }
                )

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            onAddClick = { navController.navigate("add") },
                            onEditClick = { id ->
                                navController.navigate("edit/$id")
                            }
                        )
                    }
                    composable("add") {
                        AddEditScreen(
                            viewModel = viewModel,
                            notificationId = null,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("edit/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                        AddEditScreen(
                            viewModel = viewModel,
                            notificationId = id,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}