package igor.petrov.final_android_lvl1.presentation.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.WelcomeState
import igor.petrov.final_android_lvl1.databinding.FragmentWelcomeLoadingBinding
import igor.petrov.final_android_lvl1.presentation.MainViewModel
import igor.petrov.final_android_lvl1.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWelcomeLoading.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWelcomeLoading : Fragment() {

    private var _binding: FragmentWelcomeLoadingBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val fragmentScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeLoadingBinding.inflate(inflater)

        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.filmPremierAdapter?.loadStateFlow?.onEach {
            if (it.refresh != LoadState.Loading) {
                findNavController().navigate(R.id.action_fragmentWelcomeLoading_to_navigation_home)
                mainViewModel._welcomeState.value = WelcomeState.Ready
                fragmentScope.cancel()
            }
        }?.launchIn(fragmentScope)

    }

}

