package ci.nsu.mobile.main.ui.navigation

object AppRoutes {
    object Auth {
        const val Login = "auth_login"
        const val Register = "auth_register"
    }

    object Main {
        const val Users = "main_users"
        const val Deposits = "main_deposits"
        object Deposit {
            const val Step1 = "main_calculation_step1"
            const val Step2 = "main_calculation_step2"
            const val Result = "main_calculation_result"
        }
    }
}