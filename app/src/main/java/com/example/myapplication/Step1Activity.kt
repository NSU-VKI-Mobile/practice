package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityStep1Binding
import com.example.myapplication.viewmodels.Step1ViewModel

class Step1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep1Binding
    private val viewModel: Step1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToStart.setOnClickListener { finish() }

        binding.btnNext.setOnClickListener {
            val amountStr = binding.etAmount.text.toString()
            val monthsStr = binding.etMonths.text.toString()

            if (viewModel.validate(amountStr, monthsStr)) {
                val intent = Intent(this, Step2Activity::class.java).apply {
                    putExtra("AMOUNT", amountStr.toDouble())
                    putExtra("MONTHS", monthsStr.toInt())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }
    }
}