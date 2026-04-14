package mobile.presentation
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R // Убедись, что тут твой пакет (как в MainActivity)

class MainMenuFragment : Fragment() {

    // 1. Привязываем наш XML-дизайн к этому фрагменту
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    // 2. Оживляем кнопки после того, как экран нарисовался
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val btnHistory = view.findViewById<Button>(R.id.btnHistory)
        val btnExit = view.findViewById<Button>(R.id.btnExit)


        btnCalculate.setOnClickListener {
            // Едем по стрелочке к Этапу 1
            findNavController().navigate(R.id.action_mainMenuFragment_to_stepOneFragment)
        }


        btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_historyFragment)
        }

        btnExit.setOnClickListener {
            requireActivity().finish() // Завершение работы приложения (требование препода)
        }
    }
}