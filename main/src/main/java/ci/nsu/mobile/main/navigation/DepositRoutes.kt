package ci.nsu.mobile.main.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class DepositRoutes(val route: String, val title: String) {
    object Main : DepositRoutes("main", "Расчёт вкладов")
    object FirstStep : DepositRoutes("first_step", "Основные параметры")
    object SecondStep : DepositRoutes("second_step", "Дополнительные параметры")
    object Result : DepositRoutes("result", "Результат расчёта")
    object History : DepositRoutes("history", "История расчётов")
    object HistoryDetail : DepositRoutes("history_detail/{id}", "Детали расчёта") {
        fun passId(id: Long): String = "history_detail/$id"
        const val ID_ARG = "id"

        val arguments = listOf(
            navArgument(ID_ARG) { type = NavType.LongType }
        )
    }

    companion object {
        fun fromRoute(route: String?): DepositRoutes {
            return when (route) {
                Main.route -> Main
                FirstStep.route -> FirstStep
                SecondStep.route -> SecondStep
                Result.route -> Result
                History.route -> History
                else -> Main
            }
        }

        fun extractIdFromRoute(route: String?): Long? {
            return route?.removePrefix("history_detail/")?.toLongOrNull()
        }
    }
}