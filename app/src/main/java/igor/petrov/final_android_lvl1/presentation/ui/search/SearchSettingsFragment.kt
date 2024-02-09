package igor.petrov.final_android_lvl1.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.databinding.FragmentSearchSettingsBinding

class SearchSettingsFragment : Fragment() {

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (searchViewModel.yearFrom > searchViewModel.yearTo) {
            val bufYear = searchViewModel.yearFrom
            searchViewModel.yearFrom = searchViewModel.yearTo
            searchViewModel.yearTo = bufYear
        }

        binding.radioButtonShowAll.setOnClickListener {
            setFilmType(FilmType.ALL)
        }
        binding.radioButtonShowFilms.setOnClickListener {
            setFilmType(FilmType.FILM)
        }
        binding.radioButtonShowTVSeries.setOnClickListener {
            setFilmType(FilmType.TV_SERIES)
        }
        binding.radioButtonSortRating.setOnClickListener {
            setSortingOrder(OrderType.RATING)
        }
        binding.radioButtonSortPopularity.setOnClickListener {
            setSortingOrder(OrderType.NUM_VOTE)
        }
        binding.radioButtonSortData.setOnClickListener {
            setSortingOrder(OrderType.YEAR)
        }

        binding.btnViewed.setOnClickListener {
            if (searchViewModel.showOnlyViewed) {
                searchViewModel.showOnlyViewed = false
                binding.btnViewed.setTextColor(requireContext().getColor(R.color.black))
                binding.btnViewed.backgroundTintList = requireContext().getColorStateList(R.color.white)
                binding.btnViewed.compoundDrawableTintList = requireContext().getColorStateList(R.color.black)
            } else {
                searchViewModel.showOnlyViewed = true
                binding.btnViewed.setTextColor(requireContext().getColor(R.color.white))
                binding.btnViewed.backgroundTintList = requireContext().getColorStateList(R.color.main_purple)
                binding.btnViewed.compoundDrawableTintList = requireContext().getColorStateList(R.color.white)
            }
        }

        binding.sliderRating.addOnChangeListener { slider, value, fromUser ->
            searchViewModel.ratingFrom = slider.values[0].toInt()
            searchViewModel.ratingTo = slider.values[1].toInt()
            binding.textViewSelectedRatingFrom.text = searchViewModel.ratingFrom.toString()
            binding.textViewSelectedRatingTo.text = searchViewModel.ratingTo.toString()
            if (slider.values[0] == 0f && slider.values[1] == 10f) {
                binding.textViewSelectedRating.text = getString(R.string.text_ratings_any)
            } else {
                binding.textViewSelectedRating.text = "from ${slider.values[0].toInt()} to ${slider.values[1].toInt()}"
            }
        }

        binding.textViewCountry.setOnClickListener {
            selectCountry()
        }
        binding.textViewSelectedCountry.setOnClickListener {
            selectCountry()
        }

        binding.textViewGenre.setOnClickListener {
            selectGenre()
        }

        binding.textViewSelectedGenre.setOnClickListener {
            selectGenre()
        }

        binding.textViewYear.setOnClickListener {
            selectYear()
        }
        binding.textViewSelectedYear.setOnClickListener {
            selectYear()
        }

        binding.btnApplySearchSettings.setOnClickListener {
            findNavController().popBackStack()
        }


        setFilmType(searchViewModel.filmType)
        setSortingOrder(searchViewModel.order)

        if (searchViewModel.showOnlyViewed) {
            binding.btnViewed.setTextColor(requireContext().getColor(R.color.white))
            binding.btnViewed.backgroundTintList = requireContext().getColorStateList(R.color.main_purple)
            binding.btnViewed.compoundDrawableTintList = requireContext().getColorStateList(R.color.white)
        } else {
            binding.btnViewed.setTextColor(requireContext().getColor(R.color.black))
            binding.btnViewed.backgroundTintList = requireContext().getColorStateList(R.color.white)
            binding.btnViewed.compoundDrawableTintList = requireContext().getColorStateList(R.color.black)
        }

        if (searchViewModel.ratingFrom == 0 && searchViewModel.ratingTo == 10) {
            binding.textViewSelectedRating.text = getString(R.string.text_ratings_any)
        } else {
            binding.textViewSelectedRating.text = "from ${searchViewModel.ratingFrom} to ${searchViewModel.ratingTo}"
            binding.sliderRating.values = listOf(searchViewModel.ratingFrom.toFloat(), searchViewModel.ratingTo.toFloat())
        }

        binding.textViewSelectedRatingFrom.text = searchViewModel.ratingFrom.toString()
        binding.textViewSelectedRatingTo.text = searchViewModel.ratingTo.toString()
        binding.textViewSelectedCountry.text = searchViewModel.country.country
        binding.textViewSelectedGenre.text = searchViewModel.genre.genre
        binding.textViewSelectedYear.text = "from ${searchViewModel.yearFrom} to ${searchViewModel.yearTo}"

    }

    private fun selectCountry() {
        val bundle = Bundle().apply {
            putString("label", "Select country")
            putString("selectedCountry", searchViewModel.country.country)
        }
        findNavController().navigate(R.id.action_navigation_searchSettings_to_searchSettingsSelectFromListFragment, bundle)
    }

    private fun selectGenre() {
        val bundle = Bundle().apply {
            putString("label", "Select genre")
            putString("selectedGenre", searchViewModel.genre.genre)
        }
        findNavController().navigate(R.id.action_navigation_searchSettings_to_searchSettingsSelectFromListFragment, bundle)
    }

    private fun selectYear() {
        val bundle = Bundle().apply {
            putString("label", "Select year range")
        }
        findNavController().navigate(R.id.action_searchSettingsFragment_to_searchSettingsSelectYearFragment,bundle)
    }

    private fun setFilmType(filmType: FilmType) {
        searchViewModel.filmType = filmType
        when (filmType) {
            FilmType.ALL -> {
                binding.radioButtonShowAll.isChecked = true
                binding.radioButtonShowFilms.isChecked = false
                binding.radioButtonShowTVSeries.isChecked = false
                binding.radioButtonShowAll.setTextColor(requireContext().getColor(R.color.white))
                binding.radioButtonShowFilms.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowTVSeries.setTextColor(requireContext().getColor(R.color.black))
            }

            FilmType.FILM -> {
                binding.radioButtonShowAll.isChecked = false
                binding.radioButtonShowFilms.isChecked = true
                binding.radioButtonShowTVSeries.isChecked = false
                binding.radioButtonShowAll.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowFilms.setTextColor(requireContext().getColor(R.color.white))
                binding.radioButtonShowTVSeries.setTextColor(requireContext().getColor(R.color.black))
            }

            FilmType.TV_SERIES -> {
                binding.radioButtonShowAll.isChecked = false
                binding.radioButtonShowFilms.isChecked = false
                binding.radioButtonShowTVSeries.isChecked = true
                binding.radioButtonShowAll.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowFilms.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowTVSeries.setTextColor(requireContext().getColor(R.color.white))
            }

            else -> {
                binding.radioButtonShowAll.isChecked = false
                binding.radioButtonShowFilms.isChecked = false
                binding.radioButtonShowTVSeries.isChecked = false
                binding.radioButtonShowAll.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowFilms.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonShowTVSeries.setTextColor(requireContext().getColor(R.color.black))
            }
        }
    }

    private fun setSortingOrder(orderType: OrderType) {
        searchViewModel.order = orderType
        when (orderType) {
            OrderType.RATING -> {
                binding.radioButtonSortRating.isChecked = true
                binding.radioButtonSortPopularity.isChecked = false
                binding.radioButtonSortData.isChecked = false
                binding.radioButtonSortRating.setTextColor(requireContext().getColor(R.color.white))
                binding.radioButtonSortPopularity.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortData.setTextColor(requireContext().getColor(R.color.black))
            }

            OrderType.NUM_VOTE -> {
                binding.radioButtonSortRating.isChecked = false
                binding.radioButtonSortPopularity.isChecked = true
                binding.radioButtonSortData.isChecked = false
                binding.radioButtonSortRating.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortPopularity.setTextColor(requireContext().getColor(R.color.white))
                binding.radioButtonSortData.setTextColor(requireContext().getColor(R.color.black))
            }

            OrderType.YEAR -> {
                binding.radioButtonSortRating.isChecked = false
                binding.radioButtonSortPopularity.isChecked = false
                binding.radioButtonSortData.isChecked = true
                binding.radioButtonSortRating.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortPopularity.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortData.setTextColor(requireContext().getColor(R.color.white))
            }

            else -> {
                binding.radioButtonSortRating.isChecked = false
                binding.radioButtonSortPopularity.isChecked = false
                binding.radioButtonSortData.isChecked = false
                binding.radioButtonSortRating.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortPopularity.setTextColor(requireContext().getColor(R.color.black))
                binding.radioButtonSortData.setTextColor(requireContext().getColor(R.color.black))
            }
        }
    }

}