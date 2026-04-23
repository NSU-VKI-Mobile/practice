package com.example.depositcalculator.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedDepositViewModel : ViewModel() {
    private val _initial = MutableStateFlow(0.0)
    val initial = _initial.asStateFlow()

    private val _months = MutableStateFlow(0)
    val months = _months.asStateFlow()

    private val _rate = MutableStateFlow(0.0)
    val rate = _rate.asStateFlow()

    private val _topUp = MutableStateFlow(0.0)
    val topUp = _topUp.asStateFlow()

    fun setInitial(value: Double) { _initial.value = value }
    fun setMonths(value: Int) { _months.value = value }
    fun setRate(value: Double) { _rate.value = value }
    fun setTopUp(value: Double) { _topUp.value = value }

    fun clear() {
        _initial.value = 0.0
        _months.value = 0
        _rate.value = 0.0
        _topUp.value = 0.0
    }
}