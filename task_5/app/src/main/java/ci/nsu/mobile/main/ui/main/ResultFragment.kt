package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.R
import kotlinx.coroutines.launch

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val depositViewModel: DepositViewModel by activityViewModels()

    companion object {
        fun newInstance() = ResultFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSummary = view.findViewById<TextView>(R.id.tv_summary)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val btnHome = view.findViewById<Button>(R.id.btn_home)

        tvSummary.text = mainViewModel.getCalculationSummary()

        btnSave.setOnClickListener {
            val deposit = mainViewModel.createDepositEntity()
            lifecycleScope.launch {
                depositViewModel.insertDeposit(deposit)
            }
            android.widget.Toast.makeText(requireContext(), "Расчёт сохранён", android.widget.Toast.LENGTH_SHORT).show()
        }

        btnHome.setOnClickListener {
            parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}