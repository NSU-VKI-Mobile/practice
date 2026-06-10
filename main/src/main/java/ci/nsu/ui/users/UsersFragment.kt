package ci.nsu.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.ui.common.NetworkResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModel()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvUsers = view.findViewById<RecyclerView>(R.id.rvUsers)
        val pbUsers = view.findViewById<ProgressBar>(R.id.pbUsers)
        val tvUsersError = view.findViewById<TextView>(R.id.tvUsersError)

        adapter = UsersAdapter()
        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        rvUsers.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersState.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            pbUsers.visibility = View.VISIBLE
                            rvUsers.visibility = View.GONE
                            tvUsersError.visibility = View.GONE
                        }
                        is NetworkResult.Success -> {
                            pbUsers.visibility = View.GONE
                            rvUsers.visibility = View.VISIBLE
                            adapter.submitList(result.data)
                        }
                        is NetworkResult.Error -> {
                            pbUsers.visibility = View.GONE
                            rvUsers.visibility = View.GONE
                            tvUsersError.visibility = View.VISIBLE
                            tvUsersError.text = result.message
                        }
                        null -> {}
                    }
                }
            }
        }
    }
}
