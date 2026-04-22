package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ci.nsu.mobile.main.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DepositDetailFragment : Fragment() {

    // Use activityViewModels with the specific ViewModel class
    private val depositViewModel: DepositViewModel by activityViewModels()

    private var depositId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            depositId = it.getLong(ARG_DEPOSIT_ID, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deposit_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            android.util.Log.d("DepositDetail", "Starting fragment with ID: $depositId")

        val tvInitialAmount = view.findViewById<TextView>(R.id.tv_initial_amount_value)
        val tvPeriod = view.findViewById<TextView>(R.id.tv_period_value)
        val tvInterestRate = view.findViewById<TextView>(R.id.tv_interest_rate_value)
        val tvMonthlyTopUp = view.findViewById<TextView>(R.id.tv_monthly_topup_value)
        val tvFinalAmount = view.findViewById<TextView>(R.id.tv_final_amount_value)
        val tvInterestEarned = view.findViewById<TextView>(R.id.tv_interest_earned_value)
        val tvDate = view.findViewById<TextView>(R.id.tv_date_value)
        val btnBack = view.findViewById<android.widget.Button>(R.id.btn_back_detail)

        if (depositId != -1L) {
            // Load the deposit by ID
            depositViewModel.loadDepositById(depositId)

            // Observe the selected deposit
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                    depositViewModel.selectedDeposit.collect { deposit ->
                        if (deposit != null) {
                            tvInitialAmount.text = deposit.initialAmount.toString()
                            tvPeriod.text = "${deposit.periodMonths} мес."
                            tvInterestRate.text = "${deposit.interestRate}%"
                            tvMonthlyTopUp.text = if (deposit.monthlyTopUp > 0) deposit.monthlyTopUp.toString() else "—"
                            tvFinalAmount.text = deposit.finalAmount.toString()
                            tvInterestEarned.text = deposit.interestEarned.toString()

                            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                            tvDate.text = dateFormat.format(Date(deposit.calculationDate))
                        }
                    }
                }
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        } catch (e: Exception) {
            android.util.Log.e("DepositDetail", "Error: ${e.message}", e)
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val ARG_DEPOSIT_ID = "deposit_id"

        fun newInstance(depositId: Long): DepositDetailFragment {
            val fragment = DepositDetailFragment()
            val args = Bundle()
            args.putLong(ARG_DEPOSIT_ID, depositId)
            fragment.arguments = args
            return fragment
        }
    }
}