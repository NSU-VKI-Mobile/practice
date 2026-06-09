package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Stage02ViewModel : ViewModel() {
    private val _selectedDeposit = MutableStateFlow<DepositOption?>(null)
    val selectedDeposit: StateFlow<DepositOption?> = _selectedDeposit.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()

    fun selectDeposit(deposit: DepositOption) {
        _selectedDeposit.update { deposit }
    }

    fun setExpanded(value: Boolean) {
        _expanded.update { value }
    }
}