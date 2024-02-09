package igor.petrov.final_android_lvl1.presentation.ui.showAll

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionForRecyclerView
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentShowAllBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.Staff
import igor.petrov.final_android_lvl1.entity.person.PersonFilm
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm
import igor.petrov.final_android_lvl1.presentation.adapters.FilmAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.InterestedAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.PersonFilmAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.SimilarFilmAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.StaffAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FragmentShowAll : Fragment() {

    private val fragmentShowAllViewModel: FragmentShowAllViewModel by activityViewModels()

    private var _binding: FragmentShowAllBinding? = null
    private val binding get() = _binding!!
    private val showAllFilmAdapter = PersonFilmAdapter({ personFilm: PersonFilm -> onPersonFilmClicked(personFilm) }, { personFilm: PersonFilm -> getFilmDetails(personFilm) })

    private val scale = App.applicationContext().resources.displayMetrics.density

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowAllBinding.inflate(inflater)

        arguments?.let { bundle ->
            fragmentShowAllViewModel.showType = bundle.getString("showType")
            fragmentShowAllViewModel.label = bundle.getString("label")
            fragmentShowAllViewModel.collection = bundle.getString("collection")
            fragmentShowAllViewModel.collectionId = bundle.getInt("collectionId", 0)
            fragmentShowAllViewModel.personId = bundle.getInt("personId", 0)
            fragmentShowAllViewModel.json = bundle.getString("json")

            fragmentShowAllViewModel.countries = bundle.getString("countries")?.toInt()
            fragmentShowAllViewModel.genres = bundle.getString("genres")?.toInt()
            fragmentShowAllViewModel.order = OrderType.entries.find { it.name == bundle.getString("order") }
            fragmentShowAllViewModel.type = FilmType.entries.find { it.name == bundle.getString("type") }
            fragmentShowAllViewModel.ratingFrom = bundle.getInt("ratingFrom", 0)
            fragmentShowAllViewModel.ratingTo = bundle.getInt("ratingTo", 10)
            fragmentShowAllViewModel.yearFrom = bundle.getInt("yearFrom", 1000)
            fragmentShowAllViewModel.yearTo = bundle.getInt("yearTo", 3000)
            fragmentShowAllViewModel.imdbId = bundle.getString("imdbId")
            fragmentShowAllViewModel.keyword = bundle.getString("keyword")

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (fragmentShowAllViewModel.showType) {
            "personAllFilms" -> {
                binding.chipGroupProfessionsKeysScroll.visibility = View.VISIBLE
                fragmentShowAllViewModel.getPerson()

                binding.fragmentShowAllRecyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.fragmentShowAllRecyclerView.adapter = showAllFilmAdapter

                binding.chipGroupProfessionsKeys.addChip(requireContext(), "All", "All")
                (binding.chipGroupProfessionsKeys.children.first() as Chip).setChipBackgroundColorResource(R.color.chipChecked)

                lifecycleScope.launch {
                    fragmentShowAllViewModel.person.collect { person ->
                        if (person.personId != 0) {
                            showAllFilmAdapter.setData(person.films?.distinctBy { it.filmId })
                            fragmentShowAllViewModel.personProfessionKeyList?.distinct()?.forEach { professionKey ->
                                val professionKeyText = "${professionKey} ${fragmentShowAllViewModel.personProfessionKeyList?.count { it == professionKey }}"
                                binding.chipGroupProfessionsKeys.addChip(requireContext(), professionKeyText, professionKey)
                            }
                        }
                    }
                }
                lifecycleScope.launch {
                    fragmentShowAllViewModel.loadingState.collect {
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

            "roomCollection" -> {
                binding.chipGroupProfessionsKeysScroll.visibility = View.INVISIBLE
                val collectionFilmAdapter = InterestedAdapter { item: ItemCollectionForRecyclerView -> onItemClicked(item) }
                binding.fragmentShowAllRecyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.fragmentShowAllRecyclerView.adapter = collectionFilmAdapter
                fragmentShowAllViewModel.getCollectionFilm(true)
                lifecycleScope.launch {
                    fragmentShowAllViewModel.collectionFilmList.collect {
                        collectionFilmAdapter.setData(it.toMutableList())
                    }
                }
                lifecycleScope.launch {
                    fragmentShowAllViewModel.loadingState.collect {
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

            "filmCrew" -> {
                binding.chipGroupProfessionsKeysScroll.visibility = View.INVISIBLE
                val staffAdapter = StaffAdapter { staff: Staff -> onStaffClicked(staff) }
                binding.fragmentShowAllRecyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.fragmentShowAllRecyclerView.adapter = staffAdapter
                fragmentShowAllViewModel.getStaffFromJson()

                lifecycleScope.launch {
                    fragmentShowAllViewModel.staffFromJson.collect {
                        staffAdapter.setData(it?.toMutableList())
                    }
                }
            }

            "similarFilm" -> {
                binding.chipGroupProfessionsKeysScroll.visibility = View.INVISIBLE
                val similarFilmAdapter = SimilarFilmAdapter { similarFilm: SimilarFilm -> onFilmClicked(similarFilm) }
                binding.fragmentShowAllRecyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.fragmentShowAllRecyclerView.adapter = similarFilmAdapter
                fragmentShowAllViewModel.getSimilarFilmFromJson()

                lifecycleScope.launch {
                    fragmentShowAllViewModel.similarFilmFromJson.collect {
                        similarFilmAdapter.setData(it?.toMutableList())
                    }
                }
            }


            else -> {
                binding.chipGroupProfessionsKeysScroll.visibility = View.GONE
                val showAllFilmAdapter = FilmAdapter ({film: Film -> onFilmClicked(film)},{film: Film ->  fragmentShowAllViewModel.isFilmViewed(film)} )
                binding.fragmentShowAllRecyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.fragmentShowAllRecyclerView.adapter = showAllFilmAdapter

                fragmentShowAllViewModel.getCollection()

                lifecycleScope.launch {
                    fragmentShowAllViewModel.showAllFilmList.collect {
                        showAllFilmAdapter.submitData(it)
                    }
                }
                showAllFilmAdapter.loadStateFlow.onEach {
                    activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = if (it.refresh == LoadState.Loading) View.VISIBLE else View.INVISIBLE
                }.launchIn(lifecycleScope)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFilmClicked(film: Film) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", film.kinopoiskId)
            putString("label", if (film.nameRu.isNullOrBlank()) film.nameEn else film.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
    }

    private fun onFilmClicked(similarFilm: SimilarFilm) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", similarFilm.filmId)
            putString("label", similarFilm.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
    }

    private fun onPersonFilmClicked(personFilm: PersonFilm) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", personFilm.filmId)
            putString("label", if (personFilm.nameRu.isNullOrBlank()) personFilm.nameEng else personFilm.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)

    }

    private suspend fun getFilmDetails(personFilm: PersonFilm): Film {
        return fragmentShowAllViewModel.getFilmDetails(personFilm)
    }

    private fun onItemClicked(item: ItemCollectionForRecyclerView) {
        when (item.itemType) {
            ItemCollectionType.FILM -> {
                val bundle = Bundle().apply {
                    putInt("kinopoiskId", item.itemId)
                    putString("label", item.itemName)
                }
                findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
            }

            ItemCollectionType.PERSON -> {
                val bundle = Bundle().apply {
                    putInt("personId", item.itemId)
                    putString("label", item.itemName)
                }
                findNavController().navigate(R.id.action_global_fragmentPerson, bundle)
            }
        }
    }

    private fun onStaffClicked(staff: Staff) {

        val bundle = Bundle().apply {
            putInt("personId", staff.staffId)
            putString("label", if (staff.nameRu.isNullOrBlank()) staff.nameEn else staff.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentPerson, bundle)
    }

    private fun ChipGroup.addChip(context: Context, professionKeyText: String, professionKey: String) {

        val newChip = Chip(context).apply {
            id = View.generateViewId()
            text = professionKeyText
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            width = (70 * scale + 0.5f).toInt()
            textSize = 14f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setChipBackgroundColorResource(R.color.chipUnChecked)
            addView(this)
        }
        newChip.setOnClickListener {
            binding.chipGroupProfessionsKeys.children.forEach {
                (it as Chip).setChipBackgroundColorResource(R.color.chipUnChecked)
            }
            newChip.setChipBackgroundColorResource(R.color.chipChecked)
            if (professionKey == "All") {
                showAllFilmAdapter.setData(fragmentShowAllViewModel.person.value.films?.distinctBy { it.filmId })
            } else {
                showAllFilmAdapter.setData(fragmentShowAllViewModel.person.value.films?.filter { it.professionKey == professionKey }?.distinctBy { it.filmId })
            }
        }
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
                    when (fragmentShowAllViewModel.showType) {
                        "personAllFilms" ->{fragmentShowAllViewModel.getPerson()}
                        "roomCollection" -> {fragmentShowAllViewModel.getCollectionFilm(true)}
                    }
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
                    when (fragmentShowAllViewModel.showType) {
                        "personAllFilms" ->{fragmentShowAllViewModel.getPerson()}
                        "roomCollection" -> {fragmentShowAllViewModel.getCollectionFilm(true)}
                    }
                }.show()
        }
    }

}

