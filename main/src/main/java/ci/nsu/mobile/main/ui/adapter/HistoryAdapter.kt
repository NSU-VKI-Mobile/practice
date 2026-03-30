package ci.nsu.mobile.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositCalculationEntity
import ci.nsu.mobile.main.util.formatAmount
import ci.nsu.mobile.main.util.formatDateTime

class HistoryAdapter(
    private val onItemClick: (DepositCalculationEntity) -> Unit,
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: List<DepositCalculationEntity> = emptyList()

    fun submitList(newItems: List<DepositCalculationEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_row, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textHistoryDate: TextView = itemView.findViewById(R.id.textHistoryDate)
        private val textHistoryInitialAmount: TextView = itemView.findViewById(R.id.textHistoryInitialAmount)
        private val textHistoryFinalAmount: TextView = itemView.findViewById(R.id.textHistoryFinalAmount)

        fun bind(
            entity: DepositCalculationEntity,
            onItemClick: (DepositCalculationEntity) -> Unit
        ) {
            textHistoryDate.text = formatDateTime(entity.calculationDate)
            textHistoryInitialAmount.text = "Старт: ${formatAmount(entity.initialAmount)}"
            textHistoryFinalAmount.text = "Итог: ${formatAmount(entity.finalAmount)}"
            itemView.setOnClickListener { onItemClick(entity) }
        }
    }
}

