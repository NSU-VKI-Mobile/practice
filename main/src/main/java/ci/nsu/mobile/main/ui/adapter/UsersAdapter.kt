package ci.nsu.mobile.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.network.dto.UserDto

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private var items: List<UserDto> = emptyList()

    fun submitList(list: List<UserDto>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_row, parent, false)
        return UserViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class UserViewHolder(private val container: ViewGroup) : RecyclerView.ViewHolder(container) {
        private val textLogin: TextView = container.findViewById(R.id.textUserLogin)
        private val textEmail: TextView = container.findViewById(R.id.textUserEmail)

        fun bind(item: UserDto) {
            textLogin.text = item.login ?: "login: неизвестно"
            textEmail.text = item.email ?: "email: отсутствует"
        }
    }
}
