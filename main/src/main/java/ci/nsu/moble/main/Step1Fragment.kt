package ci.nsu.moble.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep1Binding

class Step1Fragment : Fragment(R.layout.fragment_step1) {
    private val viewModel: DepositViewModel by activityViewModels()
    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStep1Binding.bind(view)

        binding.btnNext.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val months = binding.etMonths.text.toString().toIntOrNull()

            if (amount != null && months != null) {
                viewModel.amount = amount
                viewModel.months = months
                findNavController().navigate(R.id.action_step1Fragment_to_step2Fragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
