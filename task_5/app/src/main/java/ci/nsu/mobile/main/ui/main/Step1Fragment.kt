package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import ci.nsu.mobile.main.R

class Step1Fragment : Fragment(R.layout.fragment_step1) {

    private val mainViewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance() = Step1Fragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etAmount = view.findViewById<EditText>(R.id.et_start_amount)
        val etPeriod = view.findViewById<EditText>(R.id.et_period)
        val tilAmount = view.findViewById<TextInputLayout>(R.id.til_start_amount)
        val tilPeriod = view.findViewById<TextInputLayout>(R.id.til_period)
        val btnNext = view.findViewById<Button>(R.id.btn_next)
        val btnHome = view.findViewById<Button>(R.id.btn_home)
        val btnBack = view.findViewById<Button?>(R.id.btn_back)

        btnNext.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            val period = etPeriod.text.toString().toIntOrNull()
            var valid = true

            if (amount == null || amount <= 0) {
                tilAmount.error = "Введите корректную сумму"
                valid = false
            } else tilAmount.error = null

            if (period == null || period <= 0) {
                tilPeriod.error = "Введите корректный срок"
                valid = false
            } else tilPeriod.error = null

            if (valid) {
                mainViewModel.initialAmount = amount!!
                mainViewModel.periodMonths = period!!

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, Step2Fragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnHome?.setOnClickListener {
            parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }

        btnBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}