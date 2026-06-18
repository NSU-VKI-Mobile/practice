package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodel.MainViewModelFactory

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            DepositRepository(
                DepositDatabase.getDatabase(requireContext()).depositDao()
            )
        )
    }

    companion object {
        fun newInstance(
            initialAmount: Int,
            periodMonths: Int,
            interestRate: Double,
            monthlyTopUp: Int,
            finalAmount: Int,
            interestEarned: Int,
            isFromHistory: Boolean
        ): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putInt("initialAmount", initialAmount)
                    putInt("periodMonths", periodMonths)
                    putDouble("interestRate", interestRate)
                    putInt("monthlyTopUp", monthlyTopUp)
                    putInt("finalAmount", finalAmount)
                    putInt("interestEarned", interestEarned)
                    putBoolean("isFromHistory", isFromHistory)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSummary = view.findViewById<TextView>(R.id.tv_summary)
        val btnAction = view.findViewById<Button>(R.id.btn_save)
        val btnHome = view.findViewById<Button>(R.id.btn_home)

        val args = requireArguments()

        val initialAmount = args.getInt("initialAmount")
        val periodMonths = args.getInt("periodMonths")
        val interestRate = args.getDouble("interestRate")
        val monthlyTopUp = args.getInt("monthlyTopUp")
        val finalAmount = args.getInt("finalAmount")
        val interestEarned = args.getInt("interestEarned")
        val isFromHistory = args.getBoolean("isFromHistory")

        tvSummary.text = """
            Стартовый взнос: $initialAmount
            Срок вклада: $periodMonths месяцев
            Процентная ставка: $interestRate%
            Ежемесячное пополнение: $monthlyTopUp
            Итоговая сумма: $finalAmount
            Начисленные проценты: $interestEarned
        """.trimIndent()

        if (isFromHistory) {
            btnAction.text = "Удалить"

            btnAction.setOnClickListener {
                mainViewModel.deleteDeposit(
                    initialAmount,
                    periodMonths,
                    interestRate,
                    monthlyTopUp,
                    finalAmount,
                    interestEarned
                )
                requireActivity().supportFragmentManager.popBackStack()
            }

        } else {
            btnAction.text = "Сохранить"

            btnAction.setOnClickListener {
                mainViewModel.saveCalculation()
            }
        }

        btnHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}