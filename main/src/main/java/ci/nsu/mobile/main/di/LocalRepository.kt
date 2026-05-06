package ci.nsu.mobile.main.di
import androidx.compose.runtime.staticCompositionLocalOf
import ci.nsu.mobile.main.data.repository.DepositRepository

val LocalRepository = staticCompositionLocalOf<DepositRepository> {
    error("No DepositRepository provided")
}