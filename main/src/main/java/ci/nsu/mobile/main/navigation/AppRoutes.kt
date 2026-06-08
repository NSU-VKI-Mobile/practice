package ci.nsu.mobile.main.navigation

object AppRoutes {
    object Auth {
        const val Login = "auth_login"
        const val Register = "auth_register"
    }

    object Main {
        const val Users = "main_users"
        const val Deposits = "main_deposits"

        object Calculation {
            const val Step1 = "main_calculation_step1"
        }
    }
}