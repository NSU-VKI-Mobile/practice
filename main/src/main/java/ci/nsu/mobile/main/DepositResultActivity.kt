package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import java.util.Locale

class DepositResultActivity : AppCompatActivity() {

    private val viewModel: DepositResultViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())
        DepositResultViewModelFactory(repository)
    }

    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0
    private var interestRate: Double = 0.0
    private var monthlyTopUp: Double = 0.0
    private var finalAmount: Double = 0.0
    private var interestEarned: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_result)

        val tvResultInfo = findViewById<TextView>(R.id.tvResultInfo)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnGoHome = findViewById<Button>(R.id.btnGoHome)

        initialAmount = intent.getDoubleExtra(DepositStepTwoActivity.EXTRA_RESULT_INITIAL_AMOUNT, 0.0)
        periodMonths = intent.getIntExtra(DepositStepTwoActivity.EXTRA_RESULT_PERIOD_MONTHS, 0)
        interestRate = intent.getDoubleExtra(DepositStepTwoActivity.EXTRA_RESULT_INTEREST_RATE, 0.0)
        monthlyTopUp = intent.getDoubleExtra(DepositStepTwoActivity.EXTRA_RESULT_MONTHLY_TOP_UP, 0.0)

        finalAmount = viewModel.calculateFinalAmount(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp
        )

        val totalTopUps = monthlyTopUp * periodMonths
        interestEarned = finalAmount - initialAmount - totalTopUps

        tvResultInfo.text = buildResultText()

        btnSave.setOnClickListener {
            viewModel.saveCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            ) {
                btnSave.isEnabled = false
                Toast.makeText(this, "Расчёт сохранён", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun buildResultText(): String {
        return "Стартовый взнос: ${formatMoney(initialAmount)}\n" +
            "Срок вклада: $periodMonths мес.\n" +
            "Процентная ставка: ${interestRate.toInt()}%\n" +
            "Ежемесячное пополнение: ${formatMoney(monthlyTopUp)}\n\n" +
            "Итоговая сумма: ${formatMoney(finalAmount)}\n" +
            "Начисленные проценты: ${formatMoney(interestEarned)}"
    }

    private fun formatMoney(value: Double): String {
        return String.format(Locale.US, "%.2f", value)
    }
}
