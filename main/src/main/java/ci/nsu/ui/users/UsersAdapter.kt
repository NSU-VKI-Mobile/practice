package ci.nsu.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.data.remote.dto.UserResponse

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private var items = emptyList<UserResponse>()

    fun submitList(newList: List<UserResponse>) {
        this.items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        private val tvEmail = view.findViewById<TextView>(R.id.tvEmail)

        fun bind(item: UserResponse) {
            tvUsername.text = item.username
            tvEmail.text = item.email
        }
    }
}
