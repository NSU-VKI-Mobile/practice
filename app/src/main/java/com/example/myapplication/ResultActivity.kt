package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityResultBinding
import com.example.myapplication.entities.DepositEntity
import com.example.myapplication.viewmodels.ResultViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private var depositResult: DepositEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные из Intent
        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
        val months = intent.getIntExtra("MONTHS", 0)
        val rate = intent.getDoubleExtra("RATE", 0.0)
        val add = intent.getDoubleExtra("ADD", 0.0)

        // Выполняем расчет
        depositResult = viewModel.calculate(amount, months, rate, add)

        // Вывод данных в карточку
        depositResult?.let {
            binding.tvResultText.text = """
                Стартовый взнос: ${it.amount} руб.
                Срок: ${it.months} мес.
                Ставка: ${it.rate}%
                Пополнение: ${it.monthlyAdd} руб/мес.
                ---
                ИТОГО: ${"%.2f".format(it.total)} руб.
                Начислено процентов: ${"%.2f".format(it.interest)} руб.
            """.trimIndent()
        }

        binding.btnSave.setOnClickListener {
            depositResult?.let {
                viewModel.save(it)
                Toast.makeText(this, "Сохранено в историю", Toast.LENGTH_SHORT).show()
                goHome()
            }
        }

        binding.btnHome.setOnClickListener { goHome() }
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Очистить стек экранов
        startActivity(intent)
    }
}