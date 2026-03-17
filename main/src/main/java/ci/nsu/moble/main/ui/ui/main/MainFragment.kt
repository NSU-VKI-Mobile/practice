package ci.nsu.moble.main.ui.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.moble.main.ui.main.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editText)
        val button = view.findViewById<Button>(R.id.buttonAdd)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = ShoppingAdapter(
            onCheck = { viewModel.toggleItemBought(it) },
            onDelete = { viewModel.deleteItem(it) }
        )

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        button.setOnClickListener { viewModel.addItem() }

        editText.addTextChangedListener {
            viewModel.onNewItemTextChanged(it.toString())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.items)
                editText.setText(state.newItemText)
            }
        }
    }
}