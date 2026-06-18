package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.entity.CalculationResult
import ci.nsu.mobile.main.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultViewModel by viewModels()
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = args.calculationResult
        displayResult(result)

        binding.btnSave.setOnClickListener {
            viewModel.saveCalculation(result)
        }

        binding.btnToMain.setOnClickListener {
            findNavController().navigate(R.id.action_result_to_main)
        }

        viewModel.saveState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SaveState.Success -> {
                    Snackbar.make(binding.root, getString(R.string.snackbar_saved), Snackbar.LENGTH_SHORT).show()
                    binding.btnSave.isEnabled = false
                    binding.btnSave.text = "Сохранено"
                    viewModel.resetSaveState()
                }
                is SaveState.Error -> {
                    Snackbar.make(binding.root, getString(R.string.snackbar_save_error), Snackbar.LENGTH_SHORT).show()
                    viewModel.resetSaveState()
                }
                else -> {}
            }
        }

        // Восстановить состояние кнопки при повороте экрана
        if (viewModel.isSaved) {
            binding.btnSave.isEnabled = false
            binding.btnSave.text = "Сохранено"
        }
    }

    private fun displayResult(result: CalculationResult) {
        binding.tvInitialAmount.text = getString(R.string.label_initial_amount,String.format("%.2f",result.initialAmount))
        binding.tvPeriodMonths.text = getString(R.string.label_period_months, result.periodMonths)
        binding.tvInterestRate.text = getString(R.string.label_interest_rate_value, result.interestRate.toString())
        val topUp = if (result.monthlyTopUp == 0.0) "Не указано" else String.format("%.2f",result.monthlyTopUp)
        binding.tvMonthlyTopUp.text = getString(R.string.label_monthly_top_up, topUp)
        binding.tvFinalAmount.text = getString(R.string.label_final_amount,String.format("%.2f",result.finalAmount))
        binding.tvInterestEarned.text = getString(R.string.label_interest_earned, String.format("%.2f",result.interestEarned))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
