package ci.nsu.moble.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class Step2Fragment : Fragment(R.layout.fragment_step2) {
    private val viewModel: DepositViewModel by activityViewModels()
    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStep2Binding.bind(view)

        val rates = when {
            viewModel.months < 6 -> listOf(15.0)
            viewModel.months in 6..11 -> listOf(10.0)
            else -> listOf(5.0)
        }

        binding.spinnerRate.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, rates)

        binding.btnCalculate.setOnClickListener {
            viewModel.rate = binding.spinnerRate.selectedItem as Double
            viewModel.monthlyAdd = binding.etMonthlyAdd.text.toString().toDoubleOrNull() ?: 0.0
            findNavController().navigate(R.id.action_step2Fragment_to_resultFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
