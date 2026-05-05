package mobile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import mobile.DepositApp

class StepOneFragment : Fragment() {

    private lateinit var viewModel: DepositViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подключаемся к ОБЩЕЙ ViewModel через requireActivity()
        val app = requireActivity().application as DepositApp
        val factory = DepositViewModelFactory(app.repository)
        // ВАЖНО: передаем requireActivity(), чтобы ViewModel жила дольше фрагмента!
        viewModel = ViewModelProvider(requireActivity(), factory)[DepositViewModel::class.java]

        val etInitialAmount = view.findViewById<EditText>(R.id.etInitialAmount)
        val etPeriodMonths = view.findViewById<EditText>(R.id.etPeriodMonths)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnBackToMain = view.findViewById<Button>(R.id.btnBackToMain)

        // Если юзер вернулся с Этапа 2, восстанавливаем введенные данные
        if (viewModel.initialAmount > 0) {
            etInitialAmount.setText(viewModel.initialAmount.toString())
        }
        if (viewModel.periodMonths > 0) {
            etPeriodMonths.setText(viewModel.periodMonths.toString())
        }

        // Кнопка "В начало"
        btnBackToMain.setOnClickListener {
            viewModel.clearData() // Чистим данные при отмене
            findNavController().popBackStack(R.id.mainMenuFragment, false) // Возвращаемся в меню
        }

        // Кнопка "Далее"
        btnNext.setOnClickListener {
            val amountStr = etInitialAmount.text.toString()
            val monthsStr = etPeriodMonths.text.toString()

            // Валидация (проверка на пустоту)
            if (amountStr.isEmpty() || monthsStr.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Сохраняем данные в нашу ViewModel
            viewModel.initialAmount = amountStr.toDouble()
            viewModel.periodMonths = monthsStr.toInt()

            if (viewModel.periodMonths <= 0) {
                Toast.makeText(requireContext(), "Срок должен быть больше 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            findNavController().navigate(R.id.action_stepOne_to_stepTwo)
        }
    }
}