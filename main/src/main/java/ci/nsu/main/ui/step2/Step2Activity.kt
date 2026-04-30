package ci.nsu.mobile.main.ui.step2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.databinding.ActivityStep2Binding
import ci.nsu.mobile.main.model.DepositData
import ci.nsu.mobile.main.ui.result.ResultActivity
import ci.nsu.mobile.main.ui.step1.Step1Activity
import ci.nsu.mobile.main.utils.showToast
import java.util.Locale

class Step2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityStep2Binding
    private val viewModel: Step2ViewModel by viewModels()
    private lateinit var depositData: DepositData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        depositData = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("deposit_data", DepositData::class.java) ?: DepositData()
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("deposit_data") ?: DepositData()
        }

        viewModel.setDepositData(depositData)

        setupUI()
        observeViewModel()
        displaySummary()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            goBackToStep1()
        }

        binding.btnCalculate.setOnClickListener {
            if (validateAndCalculate()) {
                proceedToResult()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.availableRates.observe(this) { rates ->
            setupSpinner(rates)
        }

        viewModel.selectedRate.observe(this) { rate ->
            rate?.let {
                val rates = viewModel.availableRates.value ?: return@let
                val position = rates.indexOf(it)
                if (position >= 0) {
                    binding.spinnerRate.setSelection(position)
                }
            }
        }

        viewModel.monthlyTopUp.observe(this) { topUp ->
            topUp?.let {
                if (binding.etMonthlyTopUp.text.toString().isEmpty()) {
                    binding.etMonthlyTopUp.setText(it.toString())
                }
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearError()
            }
        }
    }

    private fun displaySummary() {
        val initialAmount = depositData.initialAmount ?: 0.0
        val periodMonths = depositData.periodMonths ?: 0

        binding.tvInitialAmount.text = String.format(Locale.US, "%,.2f ₽", initialAmount)
        binding.tvPeriodMonths.text = resources.getQuantityString(
            ci.nsu.mobile.main.R.plurals.months,
            periodMonths,
            periodMonths
        )
    }

    private fun setupSpinner(rates: List<Double>) {
        if (rates.isEmpty()) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("Нет доступных ставок")
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRate.adapter = adapter
            binding.spinnerRate.isEnabled = false
            return
        }

        val rateStrings = rates.map { String.format(Locale.US, "%.1f%%", it) }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            rateStrings
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRate.adapter = adapter
        binding.spinnerRate.isEnabled = true
    }

    private fun validateAndCalculate(): Boolean {
        val periodMonths = viewModel.periodMonths
        if (periodMonths == null || periodMonths <= 0) {
            showToast(getString(ci.nsu.mobile.main.R.string.error_period_not_specified))
            goBackToStep1()
            return false
        }

        val selectedRate = getSelectedRate()
        if (selectedRate == null) {
            showToast(getString(ci.nsu.mobile.main.R.string.error_select_rate))
            return false
        }

        viewModel.setInterestRate(selectedRate)

        val monthlyTopUpStr = binding.etMonthlyTopUp.text.toString().trim()
        val monthlyTopUp = if (monthlyTopUpStr.isNotEmpty()) {
            monthlyTopUpStr.toDoubleOrNull()
        } else null

        if (monthlyTopUpStr.isNotEmpty() && (monthlyTopUp == null || monthlyTopUp < 0)) {
            binding.etMonthlyTopUp.error = getString(ci.nsu.mobile.main.R.string.error_invalid_topup)
            return false
        }

        viewModel.setMonthlyTopUp(monthlyTopUp)

        return true
    }

    private fun getSelectedRate(): Double? {
        val position = binding.spinnerRate.selectedItemPosition
        val rates = viewModel.availableRates.value ?: return null
        return if (position >= 0 && position < rates.size) {
            rates[position]
        } else {
            null
        }
    }

    private fun proceedToResult() {
        val finalData = DepositData(
            initialAmount = viewModel.initialAmount,
            periodMonths = viewModel.periodMonths,
            interestRate = viewModel.selectedRate.value,
            monthlyTopUp = viewModel.monthlyTopUp.value
        )

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("deposit_data", finalData)
        startActivity(intent)
    }

    private fun goBackToStep1() {
        val intent = Intent(this, Step1Activity::class.java)
        intent.putExtra("deposit_data", depositData)
        startActivity(intent)
        finish()
    }
}