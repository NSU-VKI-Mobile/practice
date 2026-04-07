package ci.nsu.mobile.main.ui.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep2Binding
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModelFactory

class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositViewModel by activityViewModels {
        DepositViewModelFactory((requireActivity().application as ci.nsu.mobile.main.App).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step1 = viewModel.step1Data.value
        if (step1 == null) {
            Toast.makeText(requireContext(), "Данные первого этапа потеряны", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        val (initial, months) = step1

        val rate = when {
            months < 6 -> 15.0
            months < 12 -> 10.0
            else -> 5.0
        }

        binding.tvRate.text = "Процентная ставка: $rate%"

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_step2Fragment_to_step1Fragment)
        }

        binding.btnCalculate.setOnClickListener {
            val monthlyTopUpStr = binding.etMonthlyTopUp.text.toString().trim()
            val monthlyTopUp = if (monthlyTopUpStr.isEmpty()) null else monthlyTopUpStr.toDoubleOrNull()

            viewModel.calculate(initial, months, rate, monthlyTopUp)
            findNavController().navigate(R.id.action_step2Fragment_to_resultFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

