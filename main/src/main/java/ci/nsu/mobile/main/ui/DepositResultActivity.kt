package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.util.IntentExtras
import ci.nsu.mobile.main.util.formatAmount
import ci.nsu.mobile.main.viewmodel.DepositResultViewModel

class DepositResultActivity : AppCompatActivity() {

    private val viewModel: DepositResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_result)

        val textInitialAmount: TextView = findViewById(R.id.textInitialAmount)
        val textPeriodMonths: TextView = findViewById(R.id.textPeriodMonths)
        val textInterestRate: TextView = findViewById(R.id.textInterestRate)
        val textMonthlyTopUp: TextView = findViewById(R.id.textMonthlyTopUp)
        val textFinalAmount: TextView = findViewById(R.id.textFinalAmount)
        val textInterestEarned: TextView = findViewById(R.id.textInterestEarned)

        val buttonSave: Button = findViewById(R.id.buttonSave)
        val buttonBackHomeFromResult: Button = findViewById(R.id.buttonBackHomeFromResult)

        val initialAmount = intent.getDoubleExtra(IntentExtras.EXTRA_INITIAL_AMOUNT, Double.NaN)
        val periodMonths = intent.getIntExtra(IntentExtras.EXTRA_PERIOD_MONTHS, -1)
        val interestRate = intent.getDoubleExtra(IntentExtras.EXTRA_INTEREST_RATE, Double.NaN)
        val hasMonthlyTopUp = intent.getBooleanExtra(IntentExtras.EXTRA_HAS_MONTHLY_TOP_UP, false)
        val monthlyTopUp = if (hasMonthlyTopUp) intent.getDoubleExtra(IntentExtras.EXTRA_MONTHLY_TOP_UP, 0.0) else null
        val finalAmount = intent.getDoubleExtra(IntentExtras.EXTRA_FINAL_AMOUNT, Double.NaN)
        val interestEarned = intent.getDoubleExtra(IntentExtras.EXTRA_INTEREST_EARNED, Double.NaN)

        if (!initialAmount.isNaN() && periodMonths > 0 && !interestRate.isNaN() && !finalAmount.isNaN() && !interestEarned.isNaN()) {
            viewModel.setResultOnce(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
            )
        }

        viewModel.uiState.observe(this) { state ->
            textInitialAmount.text = "Стартовый взнос: ${state.initialAmount?.let { formatAmount(it) } ?: "—"}"
            textPeriodMonths.text = "Срок вклада: ${state.periodMonths?.toString() ?: "—"} месяцев"
            textInterestRate.text = "Процентная ставка: ${state.interestRate?.toInt()?.toString() ?: "—"}%"
            textMonthlyTopUp.text = "Ежемесячное пополнение: ${
                state.monthlyTopUp?.let { formatAmount(it) } ?: "—"
            }"
            textFinalAmount.text = "Итоговая сумма: ${state.finalAmount?.let { formatAmount(it) } ?: "—"}"
            textInterestEarned.text = "Начисленные проценты: ${state.interestEarned?.let { formatAmount(it) } ?: "—"}"
        }

        viewModel.saveResultToast.observe(this) { event ->
            val message = event.getContentIfNotHandled() ?: return@observe
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.navigationToHistory.observe(this) { event ->
            if (event.getContentIfNotHandled() == null) return@observe
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }

        buttonSave.setOnClickListener {
            viewModel.onSaveClicked()
        }

        buttonBackHomeFromResult.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

