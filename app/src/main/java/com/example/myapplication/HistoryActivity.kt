package com.example.myapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityHistoryBinding
import com.example.myapplication.entities.DepositEntity
import com.example.myapplication.viewmodels.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = HistoryAdapter { deposit ->
            showDetailDialog(deposit)
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        // Наблюдаем за данными из БД
        viewModel.allDeposits.observe(this) { list ->
            adapter.setData(list)
        }

        binding.btnBackHistory.setOnClickListener { finish() }
    }

    private fun showDetailDialog(item: DepositEntity) {
        AlertDialog.Builder(this)
            .setTitle("Детальная информация")
            .setMessage("""
                Стартовый взнос: ${item.amount}
                Срок: ${item.months} мес.
                Ставка: ${item.rate}%
                Пополнение: ${item.monthlyAdd}
                Начислено процентов: ${"%.2f".format(item.interest)}
                Итоговая сумма: ${"%.2f".format(item.total)}
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
}