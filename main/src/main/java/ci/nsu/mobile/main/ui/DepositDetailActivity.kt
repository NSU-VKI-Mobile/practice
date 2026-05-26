package ci.nsu.mobile.main.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.util.IntentExtras
import ci.nsu.mobile.main.util.formatAmount
import ci.nsu.mobile.main.util.formatDateTime
import ci.nsu.mobile.main.viewmodel.DepositDetailViewModel

class DepositDetailActivity : AppCompatActivity() {

    private val viewModel: DepositDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_detail)

        val textCalculationDate: TextView = findViewById(R.id.textCalculationDate)
        val textInitialAmount: TextView = findViewById(R.id.textInitialAmount)
        val textPeriodMonths: TextView = findViewById(R.id.textPeriodMonths)
        val textInterestRate: TextView = findViewById(R.id.textInterestRate)
        val textMonthlyTopUp: TextView = findViewById(R.id.textMonthlyTopUp)
        val textFinalAmount: TextView = findViewById(R.id.textFinalAmount)
        val textInterestEarned: TextView = findViewById(R.id.textInterestEarned)

        val id = intent.getLongExtra(IntentExtras.EXTRA_CALC_ID, -1L)
        if (id >= 0) viewModel.setIdOnce(id)

        viewModel.calculation.observe(this) { entity ->
            if (entity == null) return@observe
            textCalculationDate.text = "Дата и время: ${formatDateTime(entity.calculationDate)}"
            textInitialAmount.text = "Стартовый взнос: ${formatAmount(entity.initialAmount)}"
            textPeriodMonths.text = "Срок вклада: ${entity.periodMonths} месяцев"
            textInterestRate.text = "Процентная ставка: ${entity.interestRate.toInt()}%"
            textMonthlyTopUp.text = "Ежемесячное пополнение: ${entity.monthlyTopUp?.let { formatAmount(it) } ?: "—"}"
            textFinalAmount.text = "Итоговая сумма: ${formatAmount(entity.finalAmount)}"
            textInterestEarned.text = "Начисленные проценты: ${formatAmount(entity.interestEarned)}"
        }
    }
}

