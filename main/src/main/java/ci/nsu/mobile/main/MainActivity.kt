package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import ci.nsu.mobile.main.data.dbo.AppDatabase
import ci.nsu.mobile.main.presenation.navigation.Screen
import ci.nsu.mobile.main.presenation.repositories.DepositRepository
import ci.nsu.mobile.main.presenation.screens.HistoryScreen
import ci.nsu.mobile.main.presenation.screens.HomeScreen
import ci.nsu.mobile.main.presenation.screens.ResultScreen
import ci.nsu.mobile.main.presenation.screens.StepOneScreen
import ci.nsu.mobile.main.presenation.screens.StepTwoScreen
import ci.nsu.mobile.main.presenation.viewmodels.DepositViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        val db =
            AppDatabase
                .getDatabase(this)

        val repo =
            DepositRepository(
                db.depositDao()
            )

        val vm =
            DepositViewModel(
                repo
            )

        setContent {

            val nav =
                rememberNavController()

            NavHost(

                navController = nav,

                startDestination =
                    Screen.Home.route

            ) {

                composable(
                    Screen.Home.route
                ) {

                    HomeScreen(nav)

                }

                composable(
                    Screen.Step1.route
                ) {

                    StepOneScreen(
                        vm,
                        nav
                    )
                }

                composable(
                    Screen.Step2.route
                ) {

                    StepTwoScreen(
                        vm,
                        nav
                    )
                }

                composable(
                    Screen.Result.route
                ) {

                    ResultScreen(
                        vm,
                        nav
                    )
                }

                composable(
                    Screen.History.route
                ) {

                    HistoryScreen(vm)

                }
            }
        }
    }
}