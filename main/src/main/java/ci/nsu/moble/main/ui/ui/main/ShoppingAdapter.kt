package ci.nsu.moble.main.ui.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.moble.main.ui.main.ShoppingItem

class ShoppingAdapter(
    private val onCheck: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    private var items: List<ShoppingItem> = emptyList()

    fun submitList(newList: List<ShoppingItem>) {
        items = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val text: TextView = view.findViewById(R.id.textName)
        val delete: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.text.text = item.name
        holder.checkBox.isChecked = item.isBought

        holder.checkBox.setOnClickListener {
            onCheck(item.id)
        }

        holder.delete.setOnClickListener {
            onDelete(item.id)
        }
    }
}