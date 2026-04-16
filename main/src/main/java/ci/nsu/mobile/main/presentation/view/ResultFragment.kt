package ci.nsu.mobile.main.presentation.view
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.presentation.viewmodel.HistoryViewModel
import ci.nsu.mobile.main.presentation.viewmodel.ResultViewModel

class ResultFragment : Fragment(R.layout.fragment_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val db = AppDatabase.getDatabase(requireContext())
        val repo = DepositRepository(db.depositDao())

        val vm = ViewModelProvider(this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(c: Class<T>): T {
                    return ResultViewModel(repo) as T
                }
            }
        )[ResultViewModel::class.java]

        val a = requireArguments().getDouble("a")
        val m = requireArguments().getInt("m")
        val r = requireArguments().getDouble("r")
        val t = arguments?.getDouble("t")

        vm.calculate(a, m, r, t)

        val tv = view.findViewById<TextView>(R.id.result)

        vm.result.observe(viewLifecycleOwner) {
            tv.text = """
                Итог: ${it.finalAmount}
                Проценты: ${it.interestEarned}
            """.trimIndent()
        }

        view.findViewById<Button>(R.id.save).setOnClickListener {
            vm.save()
        }

        view.findViewById<Button>(R.id.home).setOnClickListener {

            // 🔥 ИЗМЕНЕНО
            findNavController().navigate(R.id.mainFragment)
        }
    }
}