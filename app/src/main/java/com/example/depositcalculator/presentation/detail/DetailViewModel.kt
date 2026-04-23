package com.example.depositcalculator.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.depositcalculator.domain.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DepositRepository
) : ViewModel() {

    private val _calculation = MutableStateFlow<com.example.depositcalculator.data.local.DepositCalculationEntity?>(null)
    val calculation = _calculation.asStateFlow()

    fun loadCalculation(id: Long) {
        viewModelScope.launch {
            repository.getCalculationById(id).collectLatest {
                _calculation.value = it
            }
        }
    }
}