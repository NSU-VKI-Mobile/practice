package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.dbo.AppDatabase
import com.example.myapplication.entities.DepositEntity

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.Companion.getDatabase(application).dao()

    // Получаем данные из Room в виде LiveData
    val allDeposits: LiveData<List<DepositEntity>> = dao.getAll().asLiveData()
}