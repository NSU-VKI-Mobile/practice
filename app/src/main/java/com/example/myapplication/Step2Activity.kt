package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityStep2Binding
import com.example.myapplication.viewmodels.Step2ViewModel

class Step2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Binding
    private val viewModel: Step2ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
        val months = intent.getIntExtra("MONTHS", 0)

        // Настройка выпадающего списка (ставки)
        val rates = viewModel.getRateOptions(months)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spRate.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        binding.btnFinishCalc.setOnClickListener {
            // Извлекаем числовое значение из строки "15%"
            val selectedRate = rates[binding.spRate.selectedItemPosition]
                .replace("%", "").toDouble()

            val monthlyAdd = binding.etMonthlyAdd.text.toString().toDoubleOrNull() ?: 0.0

            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("AMOUNT", amount)
                putExtra("MONTHS", months)
                putExtra("RATE", selectedRate)
                putExtra("ADD", monthlyAdd)
            }
            startActivity(intent)
        }
    }
}