package com.example.depositcalculator.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.depositcalculator.data.local.DepositCalculationEntity
import com.example.depositcalculator.domain.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: DepositRepository
) : ViewModel() {

    fun saveCalculation(calculation: DepositCalculationEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.saveCalculation(calculation)
            onSuccess()
        }
    }
}