package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import ci.nsu.mobile.main.data.database.DepositCalculation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private var calculations = listOf<DepositCalculation>()
    private val decimalFormat = DecimalFormat("#, #0.00")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun submitList(list: List<DepositCalculation>){
        calculations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding, onItemClick)
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int){
        holder.bind(calculations[position])
    }

    override fun getItemCount() = calculations.size

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding,
        private val onItemClick: (DepositCalculation) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(calculation: DepositCalculation) {
            binding.tvDate.text = dateFormat.format(Date(calculation.colculationDate))
            binding.tvInitialAmount.text = "${decimalFormat.format(calculation.finalAmount)} ₽"
            binding.tvFinalAmount.text = "${decimalFormat.format(calculation.finalAmount)} ₽"
            binding.root.setOnClickListener {
                onItemClick(calculation)
            }
        }
    }
}