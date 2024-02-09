package igor.petrov.final_android_lvl1.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentSearchBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.presentation.adapters.FilmAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.PagedLoadStateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    //private val fragmentScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job = Job()
    private val searchScope = CoroutineScope(Dispatchers.Default)

    private val filmSearchResultAdapter = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  searchViewModel.isFilmViewed(film)} )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun searchDebounced(searchText: String) {
        if (searchJob.isActive) {
            searchJob.cancel()
        }
        searchJob = searchScope.launch {
            delay(500)
            searchViewModel.keyWord = searchText
            filmSearchResultAdapter.refresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel.getFilmSearchResultList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchText.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                searchDebounced(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchDebounced(query)
                return false
            }
        })

        binding.recyclerViewFilmSearchResult.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.recyclerViewFilmSearchResult.adapter = filmSearchResultAdapter.withLoadStateFooter(PagedLoadStateAdapter())
        lifecycleScope.launch {
            searchViewModel.filmFilterList.collect {
                filmSearchResultAdapter.submitData(it)
            }
        }

        filmSearchResultAdapter.loadStateFlow.onEach {
            if (it.refresh != LoadState.Loading) {
                activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                if (filmSearchResultAdapter.itemCount == 0 && (searchViewModel.keyWord?.length ?: 0) > 1) {
                    binding.textViewSearchIsEmpty.visibility = View.VISIBLE
                } else if (it.refresh is LoadState.Error) {
                    activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                    handleError((it.refresh as LoadState.Error).error)
                } else {
                    binding.textViewSearchIsEmpty.visibility = View.INVISIBLE
                }
        } else {
                activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.VISIBLE
            }
        }.launchIn(lifecycleScope)

        val bundle = Bundle().apply {
            putString("label", "Search settings")
        }
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_search_to_searchSettingsFragment,bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFilmClicked(film: Film) {

        val bundle = Bundle().apply {
            putInt("kinopoiskId", film.kinopoiskId)
            putString("label", film.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
    }

    private fun handleError(error: Throwable) {
        if (error is KinopoiskException) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_error))
                .setCancelable(false)

            when ((error as KinopoiskException).code) {
                401 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_401))
                    materialDialog.setView(DialogError401WrongTokenBinding.inflate(layoutInflater, null, false).root) //401 view
                }
                402 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_402))
                    materialDialog.setView(DialogError402LimitReachedBinding.inflate(layoutInflater, null, false).root)
                }
                404 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_404))
                    //.setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root) //404 view
                }
                429 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_429))
                    //.setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root) //404 view
                }
                else -> {
                    materialDialog.setMessage("${resources.getString(R.string.text_error_unknown)} \ncode: ${error.code} \n\nmessage: ${error.message}")
                    materialDialog.setView(DialogError404Binding.inflate(layoutInflater, null, false).root)
                }
            }
            materialDialog.setNegativeButton(resources.getString(R.string.text_exit)) { dialog, which ->
                activity?.finish()
            }
                .setPositiveButton(resources.getString(R.string.text_retry)) { dialog, which ->
                    filmSearchResultAdapter.refresh()
                }.show()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_error))
                .setCancelable(false)
                .setMessage(error.message)
                .setView(DialogError404Binding.inflate(layoutInflater, null, false).root)
                .setNegativeButton(resources.getString(R.string.text_exit)) { dialog, which ->
                    activity?.finish()
                }
                .setPositiveButton(resources.getString(R.string.text_retry)) { dialog, which ->
                    filmSearchResultAdapter.refresh()
                }.show()
        }
    }
}