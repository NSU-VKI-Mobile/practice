package ci.nsu.mobile.main.utils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.database.DepositCalculation

class HistoryAdapter :
    ListAdapter<DepositCalculation, HistoryAdapter.VH>(Diff()) {

    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        val view = LayoutInflater.from(p.context)
            .inflate(android.R.layout.simple_list_item_1, p, false)
        return VH(view)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = getItem(pos)
        (h.itemView as TextView).text =
            "Сумма: ${item.initialAmount} → ${item.finalAmount}"
    }

    class Diff : DiffUtil.ItemCallback<DepositCalculation>() {
        override fun areItemsTheSame(a: DepositCalculation, b: DepositCalculation) = a.id == b.id
        override fun areContentsTheSame(a: DepositCalculation, b: DepositCalculation) = a == b
    }
}
