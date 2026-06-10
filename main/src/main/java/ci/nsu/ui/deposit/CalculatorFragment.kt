package ci.nsu.ui.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ci.nsu.mobile.main.R
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment() {

    // Внедряем ViewModel калькулятора через Koin
    private val viewModel: DepositViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Привязываем созданный XML-макет к фрагменту
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Элементы управления шагами
        val tvStepTitle = view.findViewById<TextView>(R.id.tvStepTitle)
        val layoutStep1 = view.findViewById<LinearLayout>(R.id.layoutStep1)
        val layoutStep2 = view.findViewById<LinearLayout>(R.id.layoutStep2)

        // Поля ввода Шага 1
        val etInitialAmount = view.findViewById<TextInputEditText>(R.id.etInitialAmount)
        val etPeriodMonths = view.findViewById<TextInputEditText>(R.id.etPeriodMonths)

        // Поля ввода Шага 2
        val etInterestRate = view.findViewById<TextInputEditText>(R.id.etInterestRate)
        val etMonthlyTopUp = view.findViewById<TextInputEditText>(R.id.etMonthlyTopUp)

        // Кнопки навигации
        val btnNextStep = view.findViewById<Button>(R.id.btnNextStep)
        val btnBackStep = view.findViewById<Button>(R.id.btnBackStep)
        val btnCalculateAndSave = view.findViewById<Button>(R.id.btnCalculateAndSave)

        // Переход на Шаг 2
        btnNextStep.setOnClickListener {
            val amountStr = etInitialAmount.text.toString()
            val periodStr = etPeriodMonths.text.toString()

            if (amountStr.isBlank() || periodStr.isBlank()) {
                Toast.makeText(requireContext(), "Заполните параметры первого шага", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tvStepTitle.text = "Шаг 2: Ставка и пополнение"
            layoutStep1.visibility = View.GONE
            layoutStep2.visibility = View.VISIBLE
        }

        // Возврат на Шаг 1
        btnBackStep.setOnClickListener {
            tvStepTitle.text = "Шаг 1: Основные параметры"
            layoutStep1.visibility = View.VISIBLE
            layoutStep2.visibility = View.GONE
        }

        // Расчёт и автоматическая отправка в Room с привязкой к User ID
        btnCalculateAndSave.setOnClickListener {
            val rateStr = etInterestRate.text.toString()
            val topUpStr = etMonthlyTopUp.text.toString()

            if (rateStr.isBlank()) {
                Toast.makeText(requireContext(), "Укажите процентную ставку", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val initialAmount = etInitialAmount.text.toString().toDouble()
            val periodMonths = etPeriodMonths.text.toString().toInt()
            val interestRate = rateStr.toDouble()
            val monthlyTopUp = if (topUpStr.isNotBlank()) topUpStr.toDouble() else null

            // Вызываем метод бизнес-логики из нашей ViewModel
            viewModel.calculateAndSave(initialAmount, periodMonths, interestRate, monthlyTopUp)

            Toast.makeText(requireContext(), "Расчёт сохранён в базу данных!", Toast.LENGTH_SHORT).show()

            // Очищаем поля ввода и сбрасываем состояние на Шаг 1 для нового расчёта
            etInitialAmount.text?.clear()
            etPeriodMonths.text?.clear()
            etInterestRate.text?.clear()
            etMonthlyTopUp.text?.clear()

            tvStepTitle.text = "Шаг 1: Основные параметры"
            layoutStep1.visibility = View.VISIBLE
            layoutStep2.visibility = View.GONE
        }
    }
}
