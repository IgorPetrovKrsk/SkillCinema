package igor.petrov.final_android_lvl1.presentation.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.FragmentWelcome1Binding
import igor.petrov.final_android_lvl1.databinding.FragmentWelcome2Binding
import igor.petrov.final_android_lvl1.databinding.FragmentWelcome3Binding
import igor.petrov.final_android_lvl1.presentation.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWelcome3.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWelcome3 : Fragment() {

    private var _binding: FragmentWelcome3Binding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcome3Binding.inflate(inflater)

        binding.viewWelcome3.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentWelcome3_to_fragmentWelcomeLoading)
        }

        return (binding.root)


    }

}