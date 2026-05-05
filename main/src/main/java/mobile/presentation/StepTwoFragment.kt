package mobile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import mobile.DepositApp

class StepTwoFragment : Fragment() {

    private lateinit var viewModel: DepositViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Снова подключаемся к нашей ОБЩЕЙ ViewModel
        val app = requireActivity().application as DepositApp
        val factory = DepositViewModelFactory(app.repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[DepositViewModel::class.java]

        val spinnerRate = view.findViewById<Spinner>(R.id.spinnerRate)
        val etMonthlyTopUp = view.findViewById<EditText>(R.id.etMonthlyTopUp)
        val btnCalculateFinal = view.findViewById<Button>(R.id.btnCalculateFinal)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        // 1. Настраиваем выпадающий список (Spinner)
        val availableRates = viewModel.getAvailableRates()
        if (availableRates.isEmpty()) {
            Toast.makeText(requireContext(), "Ошибка: некорректный срок вклада", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack() // Возвращаем назад, если срок кривой
            return
        }

        // Превращаем цифры в красивые строки с процентами
        val ratesStrings = availableRates.map { "$it %" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ratesStrings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRate.adapter = adapter

        // 2. Кнопка "Назад"
        btnBack.setOnClickListener {
            findNavController().popBackStack() // Возвращаемся на Этап 1
        }

        // 3. Кнопка "Рассчитать"
        btnCalculateFinal.setOnClickListener {
            // Сохраняем ставку
            viewModel.interestRate = availableRates[spinnerRate.selectedItemPosition]

            // Сохраняем пополнение (если пусто, то null)
            val topUpStr = etMonthlyTopUp.text.toString()
            viewModel.monthlyTopUp = if (topUpStr.isNotEmpty()) topUpStr.toDouble() else null

            // ЗАПУСКАЕМ МАТЕМАТИКУ ИЗ VIEWMODEL!
            viewModel.calculate()


             findNavController().navigate(R.id.action_stepTwo_to_resultFragment)
        }
    }
}