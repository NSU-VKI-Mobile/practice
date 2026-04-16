package ci.nsu.mobile.main.presentation.view
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.presentation.viewmodel.HistoryViewModel
import ci.nsu.mobile.main.utils.HistoryAdapter
class HistoryFragment : Fragment(R.layout.fragment_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val db = AppDatabase.getDatabase(requireContext())
        val repo = DepositRepository(db.depositDao())

        val vm = ViewModelProvider(this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(c: Class<T>): T {
                    return HistoryViewModel(repo) as T
                }
            }
        )[HistoryViewModel::class.java]

        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val adapter = HistoryAdapter()

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        vm.history.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
