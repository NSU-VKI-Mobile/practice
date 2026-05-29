package ci.nsu.moble.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun <reified T : ViewModel> NavController.getGraphViewModel(
    route: String,
    currentEntry: NavBackStackEntry // Добавляем текущую запись в качестве ключа
): T {
    // 1. Находим в стеке навигации сам граф (напр. "calculation_graph")
    val parentEntry = remember(currentEntry) {
        this.getBackStackEntry(route)
    }
    // 2. Просим Koin создать ViewModel, привязав её к этому графу
    return koinViewModel(viewModelStoreOwner = parentEntry)
}