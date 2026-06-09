package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Stage01ViewModel : ViewModel() {
    private val _initialDeposit = MutableStateFlow("")
    val initialDeposit: StateFlow<String> = _initialDeposit.asStateFlow()

    private val _termMonths = MutableStateFlow("")
    val termMonths: StateFlow<String> = _termMonths.asStateFlow()

    fun updateInitialDeposit(value: String) {
        _initialDeposit.update { value }
    }

    fun updateTermMonths(value: String) {
        _termMonths.update { value }
    }

    fun isDataValid(): Boolean {
        return initialDeposit.value.isNotEmpty() &&
                termMonths.value.isNotEmpty() &&
                initialDeposit.value.toDoubleOrNull() != null &&
                termMonths.value.toIntOrNull() != null
    }

    fun getInitialDepositDouble(): Double = initialDeposit.value.toDoubleOrNull() ?: 0.0
    fun getTermMonthsInt(): Int = termMonths.value.toIntOrNull() ?: 0

    fun restoreFromIntent(deposit: String?, term: String?) {
        deposit?.let { _initialDeposit.update { it } }
        term?.let { _termMonths.update { it } }
    }
}