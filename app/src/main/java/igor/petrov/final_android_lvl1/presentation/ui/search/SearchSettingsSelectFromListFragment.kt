package igor.petrov.final_android_lvl1.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import igor.petrov.final_android_lvl1.databinding.FragmentSearchSettingsSelectFromListBinding
import igor.petrov.final_android_lvl1.presentation.adapters.ListSelectAdapter
import igor.petrov.final_android_lvl1.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchSettingsSelectFromListFragment : Fragment() {

    private var _binding: FragmentSearchSettingsSelectFromListBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels() // homeViewModel получаем потому что там хранится список стран и жанров

    //private val fragmentScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job = Job()
    private val searchScope = CoroutineScope(Dispatchers.Main)

    private var label: String? = null
    private var selectedCountry: String? = null
    private var selectedGenre: String? = null
    private val listSelectAdapter = ListSelectAdapter { text -> onItemClicked(text) }
    private var textList = emptyList<String>()

    //private val filmSearchResultAdapter = FilmAdapter { film: Film -> onFilmClicked(film) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchSettingsSelectFromListBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun searchDebounced(searchText: String) {
        if (searchJob.isActive) {
            searchJob.cancel()
        }
        searchJob = searchScope.launch {
            delay(500)
            searchViewModel.keyWord = searchText
            val newTextList = textList.filter { it.contains(searchText,true) }
            listSelectAdapter.setData(newTextList)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            label = it.getString("label")
            selectedCountry = it.getString("selectedCountry")
            selectedGenre = it.getString("selectedGenre")
        }

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

        binding.recyclerViewSelectList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewSelectList.adapter = listSelectAdapter

        when (label) {
            "Select country" -> {
                textList = homeViewModel.genresCountriesListDto.countriesList.map { it.country }
                listSelectAdapter.selectedData = selectedCountry
                listSelectAdapter.setData(textList)
            }

            "Select genre" -> {
                textList = homeViewModel.genresCountriesListDto.genresList.map { it.genre }
                listSelectAdapter.selectedData = selectedGenre
                listSelectAdapter.setData(textList)
            }

            else -> findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchScope.cancel()
    }

    private fun onItemClicked(text: String) {
        when (label) {
            "Select country" -> {
                searchViewModel.country = homeViewModel.genresCountriesListDto.countriesList.first { it.country == text }
            }

            "Select genre" -> {
                searchViewModel.genre = homeViewModel.genresCountriesListDto.genresList.first { it.genre == text }
            }
        }
        findNavController().popBackStack()
    }
}