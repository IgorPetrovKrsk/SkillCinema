package igor.petrov.final_android_lvl1.presentation.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.FragmentWelcome1Binding
import igor.petrov.final_android_lvl1.presentation.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWelcome1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWelcome1 : Fragment() {

    private var _binding: FragmentWelcome1Binding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcome1Binding.inflate(inflater)

        binding.bntSkip.setOnClickListener {
          findNavController().navigate(R.id.action_fragmentWelcome1_to_fragmentWelcomeLoading)
        }
        binding.viewWelcome1.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentWelcome1_to_fragmentWelcome2)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        return (binding.root)


    }

}