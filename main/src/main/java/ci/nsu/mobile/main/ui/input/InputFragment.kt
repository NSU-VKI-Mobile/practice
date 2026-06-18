package ci.nsu.mobile.main.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.entity.CalculationResult
import ci.nsu.mobile.main.databinding.FragmentInputBinding
import ci.nsu.mobile.main.util.DepositCalculator
import ci.nsu.mobile.main.util.ValidationResult
import ci.nsu.mobile.main.util.Validator

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InputViewModel by viewModels()

    private val rates = listOf("5%", "10%", "15%")
    private val rateValues = listOf(5.0, 10.0, 15.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        restoreState()
        setupStep1Buttons()
        setupStep2Buttons()
        setupBackPressHandler()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRate.adapter = adapter
    }

    private fun restoreState() {
        viewModel.initialAmount.value?.let {
            if (it.isNotEmpty()) binding.etInitialAmount.setText(it)
        }
        viewModel.periodMonths.value?.let {
            if (it.isNotEmpty()) binding.etPeriodMonths.setText(it)
        }
        viewModel.monthlyTopUp.value?.let {
            if (it.isNotEmpty()) binding.etMonthlyTopUp.setText(it)
        }
        viewModel.interestRate.value?.let { rateStr ->
            val rateDouble = rateStr.toDoubleOrNull()
            if (rateDouble != null) {
                val idx = rateValues.indexOf(rateDouble)
                if (idx >= 0) binding.spinnerRate.setSelection(idx)
            }
        }
    }

    private fun setupStep1Buttons() {
        binding.btnStep1Back.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        binding.btnStep1Next.setOnClickListener {
            saveStep1ToViewModel()

            val amountValidation = Validator.validateInitialAmount(
                binding.etInitialAmount.text.toString()
            )
            if (amountValidation is ValidationResult.Invalid) {
                binding.tilInitialAmount.error = amountValidation.errorMessage
                return@setOnClickListener
            } else {
                binding.tilInitialAmount.error = null
            }

            val periodValidation = Validator.validatePeriodMonths(
                binding.etPeriodMonths.text.toString()
            )
            if (periodValidation is ValidationResult.Invalid) {
                binding.tilPeriodMonths.error = periodValidation.errorMessage
                return@setOnClickListener
            } else {
                binding.tilPeriodMonths.error = null
            }

            val months = binding.etPeriodMonths.text.toString().toIntOrNull() ?: 0
            val autoRate = viewModel.selectRateForPeriod(months)
            val rateIdx = rateValues.indexOf(autoRate)
            if (rateIdx >= 0) binding.spinnerRate.setSelection(rateIdx)
            viewModel.interestRate.value = autoRate.toString()

            binding.layoutStep1.visibility = View.GONE
            binding.layoutStep2.visibility = View.VISIBLE
        }
    }

    private fun setupStep2Buttons() {
        binding.btnStep2Back.setOnClickListener {
            binding.layoutStep2.visibility = View.GONE
            binding.layoutStep1.visibility = View.VISIBLE
        }

        binding.btnStep2Calculate.setOnClickListener {
            saveStep2ToViewModel()

            val topUpValidation = Validator.validateMonthlyTopUp(
                binding.etMonthlyTopUp.text.toString()
            )
            if (topUpValidation is ValidationResult.Invalid) {
                binding.tilMonthlyTopUp.error = topUpValidation.errorMessage
                return@setOnClickListener
            } else {
                binding.tilMonthlyTopUp.error = null
            }

            val input = viewModel.buildCalculationInput()
            val calcResult = DepositCalculator.calculate(
                initialAmount = input.initialAmount,
                periodMonths = input.periodMonths,
                interestRate = input.interestRate,
                monthlyTopUp = input.monthlyTopUp
            )

            val result = CalculationResult(
                initialAmount = input.initialAmount,
                periodMonths = input.periodMonths,
                interestRate = input.interestRate,
                monthlyTopUp = input.monthlyTopUp,
                finalAmount = calcResult.finalAmount,
                interestEarned = calcResult.interestEarned
            )

            val action = InputFragmentDirections.actionInputToResult(result)
            findNavController().navigate(action)
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.layoutStep2.visibility == View.VISIBLE) {
                        binding.layoutStep2.visibility = View.GONE
                        binding.layoutStep1.visibility = View.VISIBLE
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun saveStep1ToViewModel() {
        viewModel.initialAmount.value = binding.etInitialAmount.text.toString()
        viewModel.periodMonths.value = binding.etPeriodMonths.text.toString()
    }

    private fun saveStep2ToViewModel() {
        viewModel.monthlyTopUp.value = binding.etMonthlyTopUp.text.toString()
        val selectedIdx = binding.spinnerRate.selectedItemPosition
        if (selectedIdx >= 0 && selectedIdx < rateValues.size) {
            viewModel.interestRate.value = rateValues[selectedIdx].toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
