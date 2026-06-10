package ci.nsu.ui.deposit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.data.model.DepositCalculation

class CalculationAdapter(
    private val onDeleteClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<CalculationAdapter.CalculationViewHolder>() {

    private var items = emptyList<DepositCalculation>()

    fun submitList(newList: List<DepositCalculation>) {
        this.items = newList
        notifyDataSetChanged() // В реальном проекте лучше DiffUtil, но для простоты обновляем так
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calculation, parent, false)
        return CalculationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalculationViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onDeleteClick)
    }

    override fun getItemCount(): Int = items.size

    class CalculationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvAmount = view.findViewById<TextView>(R.id.tvAmount)
        private val tvResult = view.findViewById<TextView>(R.id.tvResult)
        private val tvDetails = view.findViewById<TextView>(R.id.tvDetails)
        private val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(item: DepositCalculation, onDeleteClick: (DepositCalculation) -> Unit) {
            tvAmount.text = "Вклад: ${item.initialAmount} руб."
            tvResult.text = "Итого: ${item.finalAmount} руб. (Доход: ${item.interestEarned})"
            tvDetails.text = "Срок: ${item.periodMonths} мес. | Ставка: ${item.interestRate}%" +
                    if (item.monthlyTopUp != null) " | Пополнение: ${item.monthlyTopUp}" else ""

            btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}
