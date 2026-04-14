package ci.nsu.mobile.main.ui.secondstep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.SecondStepFragmentBinding
import com.google.android.material.snackbar.Snackbar

class SecondStepFragment : Fragment() {

    private var _binding: SecondStepFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SecondStepViewModel

    private val initialAmount: Double by lazy { arguments?.getDouble("initialAmount") ?: 0.0 }
    private val periodMonths: Int by lazy { arguments?.getInt("periodMonths") ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SecondStepFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SecondStepViewModel::class.java]

        binding.tvInitialAmount.text = "Стартовый взнос: ${initialAmount} ₽"
        binding.tvPeriodMonths.text = "Срок: ${periodMonths} мес."

        viewModel.loadRates(periodMonths)

        viewModel.availableRates.observe(viewLifecycleOwner) { rates ->
            val ratesText = rates.map { "$it%" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ratesText)
            binding.actvInterestRate.setAdapter(adapter)
            if (rates.isNotEmpty()) {
                binding.actvInterestRate.setText(ratesText[0], false)
                viewModel.setSelectedRate(rates[0])
            }
        }

        binding.actvInterestRate.setOnItemClickListener { _, _, position, _ ->
            val rate = viewModel.availableRates.value?.get(position) ?: return@setOnItemClickListener
            viewModel.setSelectedRate(rate)
        }

        binding.etMonthlyTopUp.doAfterTextChanged { text ->
            viewModel.updateMonthlyTopUp(text.toString())
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCalculate.setOnClickListener {
            val rate = viewModel.selectedRate.value
            if (rate == null) {
                Snackbar.make(binding.root, "Выберите процентную ставку", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val monthlyTopUp = viewModel.getMonthlyTopUpAsDouble()

            val bundle = Bundle().apply {
                putDouble("initialAmount", initialAmount)
                putInt("periodMonths", periodMonths)
                putDouble("interestRate", rate)
                if (monthlyTopUp != null) {
                    putDouble("monthlyTopUp", monthlyTopUp)
                }
            }
            findNavController().navigate(R.id.action_secondStepFragment_to_resultFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}