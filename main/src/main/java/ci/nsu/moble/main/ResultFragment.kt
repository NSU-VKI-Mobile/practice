package ci.nsu.moble.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.moble.main.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {
    private val viewModel: DepositViewModel by activityViewModels()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultBinding.bind(view)

        val result = viewModel.calculate()
        binding.tvResultDetails.text = """
            Старт: ${result.initialAmount}
            Срок: ${result.months} мес.
            Ставка: ${result.rate}%
            Пополнение: ${result.monthlyAdd}
            Итог: ${String.format("%.2f", result.finalAmount)}
            Прибыль: ${String.format("%.2f", result.profit)}
        """.trimIndent()

        binding.btnSave.setOnClickListener {
            viewModel.save(result)
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        binding.btnToStart.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
