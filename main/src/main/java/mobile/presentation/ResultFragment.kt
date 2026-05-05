package mobile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import mobile.DepositApp
import java.util.Locale

class ResultFragment : Fragment() {

    private lateinit var viewModel: DepositViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as DepositApp
        val factory = DepositViewModelFactory(app.repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[DepositViewModel::class.java]

        val tvResInitial = view.findViewById<TextView>(R.id.tvResInitial)
        val tvResPeriod = view.findViewById<TextView>(R.id.tvResPeriod)
        val tvResRate = view.findViewById<TextView>(R.id.tvResRate)
        val tvResTopUp = view.findViewById<TextView>(R.id.tvResTopUp)
        val tvResInterest = view.findViewById<TextView>(R.id.tvResInterest)
        val tvResFinal = view.findViewById<TextView>(R.id.tvResFinal)

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnBackToMain = view.findViewById<Button>(R.id.btnBackToMain)

        // Берем результат из ViewModel (StateFlow)
        val result = viewModel.calculationResult.value

        if (result != null) {
            tvResInitial.text = "Стартовый взнос: ${formatMoney(result.initialAmount)}"
            tvResPeriod.text = "Срок: ${result.periodMonths} мес."
            tvResRate.text = "Ставка: ${result.interestRate}%"

            val topUpText = if (result.monthlyTopUp != null) formatMoney(result.monthlyTopUp) else "Нет"
            tvResTopUp.text = "Ежемес. пополнение: $topUpText"

            tvResInterest.text = "Начисленные проценты: +${formatMoney(result.interestEarned)}"
            tvResFinal.text = "Итоговая сумма: ${formatMoney(result.finalAmount)}"
        }

        // Кнопка Сохранить
        btnSave.setOnClickListener {
            viewModel.saveCalculation() // Сохраняем в Room DB
            Toast.makeText(requireContext(), "Расчёт сохранён в историю!", Toast.LENGTH_SHORT).show()
            btnSave.isEnabled = false // Отключаем кнопку, чтобы не сохранить дважды
        }

        // Кнопка В начало
        btnBackToMain.setOnClickListener {
            viewModel.clearData()
            findNavController().popBackStack(R.id.mainMenuFragment, false)
        }
    }

    // Вспомогательная функция для красивого вывода денег (2 знака после запятой)
    private fun formatMoney(amount: Double): String {
        return String.format(Locale.getDefault(), "%.2f ₽", amount)
    }
}