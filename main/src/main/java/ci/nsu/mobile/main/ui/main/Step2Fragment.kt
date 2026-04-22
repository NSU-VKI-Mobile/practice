package ci.nsu.mobile.main.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodel.MainViewModelFactory

class Step2Fragment : Fragment(R.layout.fragment_step2) {

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            DepositRepository(
                DepositDatabase.getDatabase(requireContext()).depositDao()
            )
        )
    }

    companion object {
        fun newInstance() = Step2Fragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.spinner_interest)
        val etTopUp = view.findViewById<EditText>(R.id.et_monthly_topup)
        val tilTopUp = view.findViewById<TextInputLayout>(R.id.til_monthly_topup)
        val btnBack = view.findViewById<Button>(R.id.btn_back)
        val btnCalculate = view.findViewById<Button>(R.id.btn_calculate)

        val period = mainViewModel.periodMonths

        val rates = listOf("5", "10", "15")
        spinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, rates)

        val recommendation = when {
            period < 6 -> "Рекомендуемый процент: 15% для срока < 6 мес."
            period in 6..11 -> "Рекомендуемый процент: 10% для срока 6-11 мес."
            period >= 12 -> "Рекомендуемый процент: 5% для срока >= 12 мес."
            else -> ""
        }

        spinner.setSelection(0)

        btnCalculate.setOnClickListener {
            val selectedRate = spinner.selectedItem.toString().toDoubleOrNull()
            val topUp = etTopUp.text.toString().toIntOrNull() ?: 0

            if (selectedRate == null) {
                tilTopUp.error = "Выберите процент"
                return@setOnClickListener
            } else tilTopUp.error = null

            val isRecommended = when {
                period < 6 && selectedRate == 15.0 -> true
                period in 6..11 && selectedRate == 10.0 -> true
                period >= 12 && selectedRate == 5.0 -> true
                else -> false
            }

            if (!isRecommended) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Выбор процента")
                    .setMessage("Вы выбрали процент $selectedRate%. $recommendation\nПродолжить?")
                    .setPositiveButton("Да") { _, _ ->
                        proceedCalculation(selectedRate, topUp)
                    }
                    .setNegativeButton("Нет", null)
                    .show()
            } else {
                proceedCalculation(selectedRate, topUp)
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun proceedCalculation(interest: Double, topUp: Int) {
        mainViewModel.interestRate = interest
        mainViewModel.monthlyTopUp = topUp
        mainViewModel.calculateFinalAmount()

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                ResultFragment.newInstance(
                    mainViewModel.initialAmount,
                    mainViewModel.periodMonths,
                    mainViewModel.interestRate,
                    mainViewModel.monthlyTopUp,
                    mainViewModel.finalAmount,
                    mainViewModel.interestEarned,
                    false
                )
            )
            .addToBackStack(null)
            .commit()
    }
}