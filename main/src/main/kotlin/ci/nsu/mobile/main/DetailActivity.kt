package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel: DepositViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel = DepositViewModel(application)

        val depositId = intent.getLongExtra("deposit_id", 0)
        val format = NumberFormat.getNumberInstance(Locale("ru", "RU"))
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

        CoroutineScope(Dispatchers.Main).launch {
            val deposit = withContext(Dispatchers.IO) {
                viewModel.repository.getDepositById(depositId)
            }

            if (deposit != null) {
                displayDeposit(deposit, format, dateFormat)
            }
        }
    }

    private fun displayDeposit(deposit: DepositCalculation, format: NumberFormat, dateFormat: SimpleDateFormat) {
        findViewById<TextView>(R.id.tvDetailDate).text = "Дата: ${dateFormat.format(Date(deposit.calculationDate))}"
        findViewById<TextView>(R.id.tvDetailInitial).text = "Стартовый взнос: ${format.format(deposit.initialAmount)} ₽"
        findViewById<TextView>(R.id.tvDetailPeriod).text = "Срок: ${deposit.periodMonths} мес."
        findViewById<TextView>(R.id.tvDetailRate).text = "Ставка: ${deposit.interestRate}%"
        findViewById<TextView>(R.id.tvDetailMonthly).text = if (deposit.monthlyTopUp != null && deposit.monthlyTopUp > 0) {
            "Пополнение: ${format.format(deposit.monthlyTopUp)} ₽/мес"
        } else {
            "Пополнение: нет"
        }
        findViewById<TextView>(R.id.tvDetailFinal).text = "Итого: ${format.format(deposit.finalAmount)} ₽"
        findViewById<TextView>(R.id.tvDetailInterest).text = "Проценты: ${format.format(deposit.interestEarned)} ₽"
    }
}