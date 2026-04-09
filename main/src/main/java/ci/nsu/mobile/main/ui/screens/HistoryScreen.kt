package ci.nsu.mobile.main.ui.screens

import androidx.compose.runtime.Composable
import ci.nsu.mobile.main.data.database.DepositCalculation

@Composable
fun HistoryScreen(
    calculations: List<DepositCalculation>,
    isLoading: Boolean,
    error: String?,
    onItemClick: (Long) -> Unit,
    onRefresh: () -> Unit
) {
    // Пока пусто
}