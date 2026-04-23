package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.DepositData
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentResultBinding
import java.text.DecimalFormat

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()
    private val decimalFormat = DecimalFormat("#,##0.00")
    private var isSaved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val depositData = arguments?.getSerializable("depositData") as DepositData

        displayResult(depositData)

        binding.btnSave.setOnClickListener {
            if (!isSaved) {
                saveCalculation(depositData)
            }
        }

        binding.btnBackToMain.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    private fun displayResult(data: DepositData) {
        binding.tvInitialAmountValue.text = "${decimalFormat.format(data.initialAmount)} ₽"
        binding.tvPeriodValue.text = "${data.periodMonths} месяцев"
        binding.tvInterestRateValue.text = "${data.interestRate}%"
        binding.tvMonthlyTopUpValue.text = data.monthlyTopUp?.let {
            "${decimalFormat.format(it)} ₽"
        } ?: "Не указано"
        binding.tvFinalAmountValue.text = "${decimalFormat.format(data.finalAmount)} ₽"
        binding.tvInterestEarnedValue.text = "${decimalFormat.format(data.interestEarned)} ₽"
    }

    private fun saveCalculation(data: DepositData) {
        val timestamp = System.currentTimeMillis()

        viewModel.saveCalculation(
            initialAmount = data.initialAmount,
            periodMonths = data.periodMonths,
            interestRate = data.interestRate,
            monthlyTopUp = data.monthlyTopUp,
            finalAmount = data.finalAmount,
            interestEarned = data.interestEarned,
            timestamp = timestamp
        )

        isSaved = true
        binding.btnSave.isEnabled = false
        binding.btnSave.text = "Сохранено!"

        Toast.makeText(requireContext(), "Расчёт сохранён", Toast.LENGTH_SHORT).show()

        binding.btnSave.postDelayed({
            findNavController().popBackStack(R.id.mainFragment, false)
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}