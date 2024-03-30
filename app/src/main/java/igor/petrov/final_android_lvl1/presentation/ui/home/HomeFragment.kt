package igor.petrov.final_android_lvl1.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.FilmCollections
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentHomeBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Country
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.Genre
import igor.petrov.final_android_lvl1.presentation.adapters.FilmAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val binding get() = _binding!!

    private var random1Country = CountryDto(null, "")
    private var random1Genre = GenreDto(null, "")
    private var random2Country = CountryDto(null, "")
    private var random2Genre = GenreDto(null, "")
    private val filmRandom1Adapter  = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )
    private val filmRandom2Adapter  = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )
    private val filmPremierAdapter  = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )
    private val filmPopularAdapter  = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )
    private val filmTop250Adapter   = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )
    private val serialAdapter       = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  homeViewModel.isFilmViewed(film)} )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFilmPremier.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmPremier.adapter = filmPremierAdapter

        lifecycleScope.launch {
            homeViewModel.filmPremieresList.collect {
                filmPremierAdapter.submitData(it)
            }
        }

        filmPremierAdapter.loadStateFlow.onEach {
            if (it.refresh is LoadState.Error) {
                val error = (it.refresh as LoadState.Error).error
                handleError(error)
            }
        }.launchIn(lifecycleScope)


        binding.recyclerViewFilmPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmPopular.adapter = filmPopularAdapter
        lifecycleScope.launch {
            homeViewModel.filmPopularList.collect {
                filmPopularAdapter.submitData(it)
            }
        }

        binding.recyclerViewFilmRandom1.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmRandom1.adapter = filmRandom1Adapter
        lifecycleScope.launch {
            homeViewModel.filmRandom1List.collect {
                random1Country = homeViewModel.random1Country
                random1Genre = homeViewModel.random1Genre
                binding.textViewRandom1.text = getString(R.string.text_country) + homeViewModel.random1Country.country + "\n" + getString(R.string.text_genre) + homeViewModel.random1Genre.genre
                filmRandom1Adapter.submitData(it)
            }
        }

        binding.recyclerViewFilmRandom2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmRandom2.adapter = filmRandom2Adapter
        lifecycleScope.launch {
            homeViewModel.filmRandom2List.collect {
                random2Country = homeViewModel.random2Country
                random2Genre = homeViewModel.random2Genre
                binding.textViewRandom2.text = getString(R.string.text_country) + homeViewModel.random2Country.country + "\n" + getString(R.string.text_genre) + homeViewModel.random2Genre.genre
                filmRandom2Adapter.submitData(it)
            }
        }

        binding.recyclerViewFilmTop250.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmTop250.adapter = filmTop250Adapter
        lifecycleScope.launch {
            homeViewModel.filmTop250List.collect {
                filmTop250Adapter.submitData(it)
            }
        }

        binding.recyclerViewFilmSerials.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmSerials.adapter = serialAdapter
        lifecycleScope.launch {
            homeViewModel.serialList.collect {
                serialAdapter.submitData(it)
            }
        }


        binding.btnShowAllPremires.setOnClickListener {
            showAllPremieres()
        }

        binding.btnShowAllPopular.setOnClickListener {
            showAllCollection(FilmCollections.TOP_POPULAR_ALL)
        }

        binding.btnShowAllTop250.setOnClickListener {
            showAllCollection((FilmCollections.TOP_250_MOVIES))
        }

        binding.btnShowAllSerials.setOnClickListener {
            showAllFilter(CountryDto(null, ""), GenreDto(null, ""), FilmType.TV_SERIES)
        }

        binding.btnShowAllRandom1.setOnClickListener {
            showAllFilter(random1Country, random1Genre, null)
        }

        binding.btnShowAllRandom2.setOnClickListener {
            showAllFilter(random2Country, random2Genre, null)
        }

        lifecycleScope.launch {
            homeViewModel.loadingState.collect {
                when (it) {
                    is LoadingState.Error -> {
                        activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                        handleError(it.kinopoiskException)
                    }

                    LoadingState.Loading -> {
                        activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.VISIBLE
                    }

                    LoadingState.Ready -> {
                        activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                    }

                    else -> {
                        activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                    }
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewFilmPremier.adapter = null
        binding.recyclerViewFilmPopular.adapter = null
        binding.recyclerViewFilmRandom1.adapter = null
        binding.recyclerViewFilmRandom2.adapter = null
        binding.recyclerViewFilmTop250.adapter = null
        binding.recyclerViewFilmSerials.adapter = null
        _binding = null

    }

    private fun showAllFilter(country: Country?, genre: Genre?, type: FilmType?) {
        val bundle = Bundle().apply {
            putString("showType", "filter")
            if (country?.id != null && genre?.id != null) {
                putString("countries", country.id.toString())
                putString("genres", genre.id.toString())
                putString("label", getString(R.string.text_all) + " " + getString(R.string.text_country) + country.country + "\n" + getString(R.string.text_genre) + genre.genre)
            } else {
                putString("label", getString(R.string.text_all) + type?.label)
                putString("type", type?.name)
            }
        }
        findNavController().navigate(R.id.action_navigation_home_to_fragmentShowAll, bundle)
    }

    private fun showAllCollection(filmCollections: FilmCollections) {
        val bundle = Bundle().apply {
            putString("showType", "collection")
            putString("collection", filmCollections.name)
            putString("label", filmCollections.label)
        }
        findNavController().navigate(R.id.action_navigation_home_to_fragmentShowAll, bundle)
    }


    private fun showAllPremieres() {
        val bundle = Bundle().apply {
            putString("showType", "premieres")
            putString("collection", "Premieres")
            putString("label", "Premieres")
        }
        findNavController().navigate(R.id.action_navigation_home_to_fragmentShowAll, bundle)
    }

    private fun refreshRandomList(film: Film) {
        if (random1Country == film.countries?.first() && random1Genre == film.genres?.first()) {
            homeViewModel.restartRandom1List()
            filmRandom1Adapter.refresh()
        } else {
            homeViewModel.restartRandom2List()
            filmRandom2Adapter.refresh()
        }
    }

    private fun openFragmentWithFilm(film: Film) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", film.kinopoiskId)
            putString("label", film.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
    }

    private fun onFilmClicked(film: Film) {

        when (film.premiereRu) {
            "showAllPremieres" -> showAllPremieres()
            "showAllFilter" -> showAllFilter(film.countries?.first(), film.genres?.first(), film.type)
            "refresh" -> refreshRandomList(film)
            FilmCollections.TOP_POPULAR_ALL.name -> showAllCollection(FilmCollections.TOP_POPULAR_ALL)
            FilmCollections.TOP_250_MOVIES.name -> showAllCollection(FilmCollections.TOP_250_MOVIES)
            else -> openFragmentWithFilm(film)
        }

    }

    private fun handleError(error: Throwable) {
        if (error is KinopoiskException) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_error))
                .setCancelable(false)

            when (error.code) {
                401 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_401))
                    materialDialog.setView(DialogError401WrongTokenBinding.inflate(layoutInflater, null, false).root)
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
                    homeViewModel.getGenresAndCountries()
                    filmPremierAdapter.refresh()
                    filmPopularAdapter.refresh()
                    serialAdapter.refresh()
                    filmTop250Adapter.refresh()
                    filmRandom1Adapter.refresh()
                    filmRandom2Adapter.refresh()
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
                    homeViewModel.getGenresAndCountries()
                    filmPremierAdapter.refresh()
                    filmPopularAdapter.refresh()
                    serialAdapter.refresh()
                    filmTop250Adapter.refresh()
                    filmRandom1Adapter.refresh()
                    filmRandom2Adapter.refresh()
                }.show()
        }
    }


}