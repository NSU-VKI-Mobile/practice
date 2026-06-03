package ci.nsu.mobile.main.ui.secondstep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentSecondStepBinding
import ci.nsu.mobile.main.ui.firststep.FirstStepViewModel
import ci.nsu.mobile.main.utils.Validator

class SecondStepFragment : Fragment() {

    private var _binding: FragmentSecondStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SecondStepViewModel
    private lateinit var firstStepViewModel: FirstStepViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SecondStepViewModel::class.java]
        firstStepViewModel = ViewModelProvider(requireActivity())[FirstStepViewModel::class.java]

        val periodMonths = firstStepViewModel.getPeriodMonths()
        val availableRate = Validator.calculateInterestRate(periodMonths)

        // Настройка выпадающего списка
        val rates = listOf("$availableRate%")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rates)
        binding.interestRateSpinner.setAdapter(adapter)
        binding.interestRateSpinner.setText("$availableRate%", false)

        binding.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_secondStepFragment_to_firstStepFragment)
        }

        binding.calculateButton.setOnClickListener {
            val monthlyTopUp = binding.monthlyTopUpInput.text.toString()
            val topUpResult = Validator.validateMonthlyTopUp(monthlyTopUp)

            if (topUpResult is Validator.Result.Error) {
                showError(topUpResult.message)
            } else {
                val initialAmount = firstStepViewModel.getInitialAmount()
                val periodMonths = firstStepViewModel.getPeriodMonths()
                val interestRate = availableRate
                val monthlyTopUpValue = (topUpResult as Validator.Result.Success).data

                // ЛОГИРОВАНИЕ для проверки
                android.util.Log.d("SecondStep", "initialAmount: $initialAmount")
                android.util.Log.d("SecondStep", "periodMonths: $periodMonths")
                android.util.Log.d("SecondStep", "interestRate: $interestRate")
                android.util.Log.d("SecondStep", "monthlyTopUpValue: $monthlyTopUpValue")

                val result = Validator.calculateDeposit(
                    initialAmount,
                    periodMonths,
                    interestRate,
                    monthlyTopUpValue
                )

                android.util.Log.d("SecondStep", "finalAmount: ${result.finalAmount}")
                android.util.Log.d("SecondStep", "interestEarned: ${result.interestEarned}")

                viewModel.saveResult(
                    initialAmount,
                    periodMonths,
                    interestRate,
                    monthlyTopUpValue,
                    result.finalAmount,
                    result.interestEarned
                )

                view.findNavController().navigate(R.id.action_secondStepFragment_to_resultFragment)
            }
        }
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}