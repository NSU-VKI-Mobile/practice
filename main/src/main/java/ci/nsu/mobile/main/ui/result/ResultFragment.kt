package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentResultBinding
import ci.nsu.mobile.main.data.model.DepositResultData
import java.text.DecimalFormat

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ResultViewModel
    private val args: ResultFragmentArgs by navArgs()
    private val df = DecimalFormat("#.##")

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

        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)

        val inputData = args.resultData
        viewModel.calculateAndShow(inputData)

        viewModel.resultData.observe(viewLifecycleOwner) { data ->
            if (data != null) updateUI(data)
        }

        // БЛОКИРОВКА КНОПКИ ПОСЛЕ СОХРАНЕНИЯ
        viewModel.isSaved.observe(viewLifecycleOwner) { saved ->
            binding.buttonSave.isEnabled = !saved
            binding.buttonSave.text = if (saved) "Сохранено" else "Сохранить"
        }

        binding.buttonSave.setOnClickListener {
            viewModel.resultData.value?.let { data ->
                viewModel.saveCalculation(data)
                Toast.makeText(requireContext(), "Расчёт сохранён", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonToStart.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    private fun updateUI(data: DepositResultData) {
        binding.tvInitialAmount.text = "${df.format(data.initialAmount)} ₽"
        binding.tvPeriodMonths.text = "${data.periodMonths} мес."
        binding.tvInterestRate.text = "${data.interestRate}%"
        binding.tvMonthlyTopUp.text = if (data.monthlyTopUp != null && data.monthlyTopUp > 0)
            "${df.format(data.monthlyTopUp)} ₽" else "не указано"
        binding.tvFinalAmount.text = "${df.format(data.finalAmount)} ₽"
        binding.tvInterestEarned.text = "${df.format(data.interestEarned)} ₽"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}