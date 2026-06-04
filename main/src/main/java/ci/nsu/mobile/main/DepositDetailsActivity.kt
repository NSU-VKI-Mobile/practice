package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DepositDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_details)

        val tvDetailsInfo = findViewById<TextView>(R.id.tvDetailsInfo)
        val btnDetailsBack = findViewById<Button>(R.id.btnDetailsBack)
        val calculationId = intent.getLongExtra(EXTRA_CALCULATION_ID, -1L)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())
        val viewModel = DepositDetailsViewModel(repository)

        if (calculationId == -1L) {
            tvDetailsInfo.text = "Расчёт не найден"
        } else {
            lifecycleScope.launch {
                viewModel.getCalculationById(calculationId).collectLatest { calculation ->
                    tvDetailsInfo.text = if (calculation == null) {
                        "Расчёт не найден"
                    } else {
                        buildDetailsText(calculation)
                    }
                }
            }
        }

        btnDetailsBack.setOnClickListener {
            finish()
        }
    }

    private fun buildDetailsText(calculation: DepositCalculation): String {
        val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(calculation.calculationDate))

        return "Дата расчёта: $date\n\n" +
            "Стартовый взнос: ${formatMoney(calculation.initialAmount)}\n" +
            "Срок вклада: ${calculation.periodMonths} мес.\n" +
            "Процентная ставка: ${calculation.interestRate.toInt()}%\n" +
            "Ежемесячное пополнение: ${formatMoney(calculation.monthlyTopUp)}\n\n" +
            "Итоговая сумма: ${formatMoney(calculation.finalAmount)}\n" +
            "Начисленные проценты: ${formatMoney(calculation.interestEarned)}"
    }

    private fun formatMoney(value: Double): String {
        return String.format(Locale.US, "%.2f", value)
    }

    companion object {
        const val EXTRA_CALCULATION_ID = "calculation_id"
    }
}
