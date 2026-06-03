package ci.nsu.mobile.main.ui.firststep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentFirstStepBinding
import ci.nsu.mobile.main.utils.Validator

class FirstStepFragment : Fragment() {

    private var _binding: FragmentFirstStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FirstStepViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FirstStepViewModel::class.java]

        binding.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_firstStepFragment_to_mainFragment)
        }

        binding.nextButton.setOnClickListener {
            val initialAmount = binding.initialAmountInput.text.toString()
            val periodMonths = binding.periodInput.text.toString()

            val amountResult = Validator.validateInitialAmount(initialAmount)
            val periodResult = Validator.validatePeriodMonths(periodMonths)

            when {
                amountResult is Validator.Result.Error -> {
                    showError(amountResult.message)
                }
                periodResult is Validator.Result.Error -> {
                    showError(periodResult.message)
                }
                amountResult is Validator.Result.Success && periodResult is Validator.Result.Success -> {
                    viewModel.saveInitialData(amountResult.data, periodResult.data)
                    view.findNavController().navigate(R.id.action_firstStepFragment_to_secondStepFragment)
                }
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