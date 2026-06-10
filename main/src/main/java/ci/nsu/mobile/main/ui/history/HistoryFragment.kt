package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentHistoryBinding
import ci.nsu.mobile.main.data.database.DepositEntity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Кнопка "В начало"
        binding.buttonToStart.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        // Кнопка "Очистить всё"
        binding.buttonClearAll.setOnClickListener {
            if (adapter.itemCount == 0) {
                Toast.makeText(requireContext(), "История пуста", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Очистить историю")
                .setMessage("Вы уверены, что хотите удалить все расчёты?")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteAll()
                    Toast.makeText(requireContext(), "История очищена", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Нет", null)
                .show()
        }

        // Адаптер (без долгого нажатия, только клик для просмотра)
        adapter = HistoryAdapter { entity ->
            val bundle = Bundle().apply { putSerializable("entity", entity) }
            findNavController().navigate(R.id.action_history_to_detail, bundle)
        }

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHistory.adapter = adapter

        // Свайп для удаления
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val entity = adapter.getItemAt(position)

                // Временно сохраняем элемент для возможной отмены
                viewModel.deleteCalculation(entity)
                Toast.makeText(requireContext(), "Расчёт удалён", Toast.LENGTH_SHORT).show()

                // Snackbar с отменой удаления
                Snackbar.make(binding.root, "Расчёт удалён", Snackbar.LENGTH_LONG)
                    .setAction("Отмена") {
                        viewModel.restoreCalculation(entity)
                        Toast.makeText(requireContext(), "Восстановлено", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        }

        ItemTouchHelper(swipeToDelete).attachToRecyclerView(binding.recyclerViewHistory)

        // Наблюдение за списком
        viewModel.calculations.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) {
                Toast.makeText(requireContext(), "Нет сохранённых расчётов", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(java.util.Date(timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}