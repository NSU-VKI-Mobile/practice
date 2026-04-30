package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.ActivityHistoryDetailBinding
import ci.nsu.mobile.main.model.DepositCalculation
import ci.nsu.mobile.main.ui.main.MainActivity
import ci.nsu.mobile.main.utils.showToast
import kotlinx.coroutines.launch
import java.util.Locale

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private val viewModel: HistoryDetailViewModel by viewModels()
    private var calculationId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calculationId = intent.getLongExtra("calculation_id", -1)

        if (calculationId == -1L) {
            showToast("Ошибка: расчёт не найден")
            finish()
            return
        }

        setupUI()
        loadCalculation()
    }

    private fun setupUI() {
        binding.btnBackToStart.setOnClickListener {
            goToMainScreen()
        }

        binding.btnDelete.setOnClickListener {
            deleteCalculation()
        }
    }

    private fun loadCalculation() {
        lifecycleScope.launch {
            val calculation = viewModel.getCalculationById(calculationId)
            if (calculation != null) {
                displayCalculation(calculation)
            } else {
                showToast("Расчёт не найден")
                finish()
            }
        }
    }

    private fun displayCalculation(calculation: DepositCalculation) {
        binding.apply {
            tvDateValue.text = calculation.getFormattedDate()
            tvInitialAmountValue.text = formatCurrency(calculation.initialAmount)
            tvPeriodValue.text = "${calculation.periodMonths} месяцев"
            tvInterestRateValue.text = String.format(Locale.US, "%.1f%%", calculation.interestRate)

            val monthlyTopUp = calculation.monthlyTopUp
            if (monthlyTopUp != null && monthlyTopUp > 0) {
                tvMonthlyTopUpValue.text = formatCurrency(monthlyTopUp)
            } else {
                tvMonthlyTopUpValue.text = "Не указано"
            }

            tvFinalAmountValue.text = formatCurrency(calculation.finalAmount)
            tvInterestEarnedValue.text = formatCurrency(calculation.interestEarned)

            if (calculation.interestEarned > 0) {
                tvInterestEarnedValue.setTextColor(getColor(android.R.color.holo_green_dark))
            } else if (calculation.interestEarned < 0) {
                tvInterestEarnedValue.setTextColor(getColor(android.R.color.holo_red_dark))
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        return String.format(Locale.US, "%,.2f ₽", amount)
    }

    private fun deleteCalculation() {
        lifecycleScope.launch {
            val success = viewModel.deleteCalculation(calculationId)
            if (success) {
                showToast("Расчёт удалён")
                goToMainScreen()
            } else {
                showToast("Ошибка при удалении")
            }
        }
    }

    private fun goToMainScreen() {
        val intent = android.content.Intent(this, MainActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}