package ci.nsu.mobile.main.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.databinding.ActivityResultBinding
import ci.nsu.mobile.main.model.DepositCalculation
import ci.nsu.mobile.main.model.DepositData
import ci.nsu.mobile.main.ui.main.MainActivity
import ci.nsu.mobile.main.utils.showToast
import kotlinx.coroutines.launch
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private lateinit var depositData: DepositData
    private var finalAmount: Double = 0.0
    private var interestEarned: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Исправленный способ получения Parcelable
        depositData = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("deposit_data", DepositData::class.java) ?: DepositData()
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("deposit_data") ?: DepositData()
        }

        calculateResults()
        displayResults()
        setupUI()
    }

    private fun calculateResults() {
        val initialAmount = depositData.initialAmount ?: 0.0
        val periodMonths = depositData.periodMonths ?: 0
        val interestRate = depositData.interestRate ?: 0.0
        val monthlyTopUp = depositData.monthlyTopUp ?: 0.0

        val monthlyRate = interestRate / 100 / 12
        var amount = initialAmount

        for (month in 1..periodMonths) {
            amount += monthlyTopUp
            amount += amount * monthlyRate
        }

        finalAmount = amount
        interestEarned = finalAmount - initialAmount - (monthlyTopUp * periodMonths)

        viewModel.setCalculationData(depositData, finalAmount, interestEarned)
    }

    private fun displayResults() {
        binding.tvInitialAmountValue.text = formatCurrency(depositData.initialAmount ?: 0.0)
        binding.tvPeriodValue.text = "${depositData.periodMonths ?: 0} месяцев"
        binding.tvInterestRateValue.text = String.format(Locale.US, "%.1f%%", depositData.interestRate ?: 0.0)

        val monthlyTopUp = depositData.monthlyTopUp
        if (monthlyTopUp != null && monthlyTopUp > 0) {
            binding.tvMonthlyTopUpValue.text = formatCurrency(monthlyTopUp)
        } else {
            binding.tvMonthlyTopUpValue.text = "Не указано"
        }

        binding.tvFinalAmountValue.text = formatCurrency(finalAmount)
        binding.tvInterestEarnedValue.text = formatCurrency(interestEarned)

        if (interestEarned > 0) {
            binding.tvInterestEarnedValue.setTextColor(getColor(android.R.color.holo_green_dark))
        } else if (interestEarned < 0) {
            binding.tvInterestEarnedValue.setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }

    private fun formatCurrency(amount: Double): String {
        return String.format(Locale.US, "%,.2f ₽", amount)
    }

    private fun setupUI() {
        binding.btnSave.setOnClickListener {
            saveCalculation()
            goToMainScreen()
        }

        binding.btnBackToStart.setOnClickListener {
            goToMainScreen()
        }
    }

    private fun saveCalculation() {
        val calculation = DepositCalculation(
            initialAmount = depositData.initialAmount ?: 0.0,
            periodMonths = depositData.periodMonths ?: 0,
            interestRate = depositData.interestRate ?: 0.0,
            monthlyTopUp = depositData.monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            val success = viewModel.saveCalculation(calculation)
            if (success) {
                showToast("Расчёт успешно сохранён!")
            } else {
                showToast("Ошибка при сохранении расчёта")
            }
        }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}