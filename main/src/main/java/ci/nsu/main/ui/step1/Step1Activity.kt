package ci.nsu.mobile.main.ui.step1

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.databinding.ActivityStep1Binding
import ci.nsu.mobile.main.model.DepositData
import ci.nsu.mobile.main.ui.main.MainActivity
import ci.nsu.mobile.main.ui.step2.Step2Activity

class Step1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityStep1Binding
    private val viewModel: Step1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedData = intent.getParcelableExtra<DepositData>("deposit_data")
        savedData?.let { viewModel.restoreData(it) }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnBackToStart.setOnClickListener {
            goToMainScreen()
        }

        binding.btnNext.setOnClickListener {
            if (validateInputs()) {
                saveDataAndProceed()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.initialAmount.observe(this) { amount ->
            amount?.let {
                if (binding.etInitialAmount.text.toString().isEmpty()) {
                    binding.etInitialAmount.setText(it.toString())
                }
            }
        }

        viewModel.periodMonths.observe(this) { months ->
            months?.let {
                if (binding.etPeriodMonths.text.toString().isEmpty()) {
                    binding.etPeriodMonths.setText(it.toString())
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val initialAmountStr = binding.etInitialAmount.text.toString().trim()
        val periodMonthsStr = binding.etPeriodMonths.text.toString().trim()

        if (initialAmountStr.isEmpty()) {
            binding.etInitialAmount.error = "Введите стартовый взнос"
            binding.etInitialAmount.requestFocus()
            return false
        }

        val initialAmount = initialAmountStr.toDoubleOrNull()
        if (initialAmount == null || initialAmount <= 0) {
            binding.etInitialAmount.error = "Введите корректную сумму (больше 0)"
            binding.etInitialAmount.requestFocus()
            return false
        }

        if (periodMonthsStr.isEmpty()) {
            binding.etPeriodMonths.error = "Введите срок вклада"
            binding.etPeriodMonths.requestFocus()
            return false
        }

        val periodMonths = periodMonthsStr.toIntOrNull()
        if (periodMonths == null || periodMonths <= 0) {
            binding.etPeriodMonths.error = "Введите корректный срок (больше 0)"
            binding.etPeriodMonths.requestFocus()
            return false
        }

        viewModel.setInitialAmount(initialAmount)
        viewModel.setPeriodMonths(periodMonths)

        return true
    }

    private fun saveDataAndProceed() {
        val depositData = DepositData(
            initialAmount = viewModel.initialAmount.value,
            periodMonths = viewModel.periodMonths.value,
            interestRate = null,
            monthlyTopUp = null
        )

        val intent = Intent(this, Step2Activity::class.java)
        intent.putExtra("deposit_data", depositData)
        startActivity(intent)
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("initial_amount", viewModel.initialAmount.value ?: 0.0)
        outState.putInt("period_months", viewModel.periodMonths.value ?: 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val initialAmount = savedInstanceState.getDouble("initial_amount")
        val periodMonths = savedInstanceState.getInt("period_months")
        if (initialAmount > 0) viewModel.setInitialAmount(initialAmount)
        if (periodMonths > 0) viewModel.setPeriodMonths(periodMonths)
    }
}