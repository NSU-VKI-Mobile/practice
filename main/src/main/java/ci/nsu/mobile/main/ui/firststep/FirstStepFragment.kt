package ci.nsu.mobile.main.ui.firststep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FirstStepFragmentBinding
import com.google.android.material.snackbar.Snackbar

class FirstStepFragment : Fragment() {

    private var _binding: FirstStepFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FirstStepViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstStepFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FirstStepViewModel::class.java]

        binding.etInitialAmount.setText(viewModel.initialAmount.value ?: "")
        binding.etPeriodMonths.setText(viewModel.periodMonths.value ?: "")

        binding.etInitialAmount.setOnFocusChangeListener { _, _ ->
            viewModel.updateInitialAmount(binding.etInitialAmount.text.toString())
        }
        binding.etPeriodMonths.setOnFocusChangeListener { _, _ ->
            viewModel.updatePeriodMonths(binding.etPeriodMonths.text.toString())
        }

        binding.btnBackToMain.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.isValid.value == true) {
                val amount = binding.etInitialAmount.text.toString().toDouble()
                val months = binding.etPeriodMonths.text.toString().toInt()
                val bundle = Bundle().apply {
                    putDouble("initialAmount", amount)
                    putInt("periodMonths", months)
                }
                findNavController().navigate(R.id.action_firstStepFragment_to_secondStepFragment, bundle)
            } else {
                Snackbar.make(binding.root, "Заполните все поля корректно", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}