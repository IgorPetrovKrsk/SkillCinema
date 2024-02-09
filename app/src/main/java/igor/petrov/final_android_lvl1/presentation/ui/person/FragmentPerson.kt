package igor.petrov.final_android_lvl1.presentation.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stfalcon.imageviewer.StfalconImageViewer
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentPersonBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.person.PersonFilm
import igor.petrov.final_android_lvl1.presentation.adapters.PersonFilmAdapter
import kotlinx.coroutines.launch


class FragmentPerson : Fragment() {

    private val fragmentPersonViewModel: FragmentPersonViewModel by activityViewModels()
    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPersonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            fragmentPersonViewModel.personId = it.getInt("personId")
        }
        fragmentPersonViewModel.getPerson()

        val personFilmAdapter = PersonFilmAdapter({ personFilm: PersonFilm -> onFilmClicked(personFilm) }, { personFilm: PersonFilm -> getFilmDetails(personFilm) })
        binding.recyclerViewPersonBestFilms.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPersonBestFilms.adapter = personFilmAdapter

        lifecycleScope.launch {
            fragmentPersonViewModel.person.collect { person ->
                if (person.personId != 0) {
                    binding.textViewPersonName.text = if (person.nameRu.isNullOrBlank()) person.nameEn else person.nameRu
                    binding.textViewPersonJob.text = "${person.profession ?: "---"}"
                    binding.textViewPersonMovieListNumber.text = "Total films: ${person.films?.size ?: 0}"

                    Glide.with(binding.root).load(person.posterUrl).into(binding.imageViewPersonPoster)
                    val sortedPersonFilmList = person.films?.sortedByDescending {it.rating}?.distinctBy { it.filmId }?.take(10)

                    personFilmAdapter.setData(sortedPersonFilmList)

                    binding.btnShowAllMovieList.setOnClickListener {
                        val bundle = Bundle().apply {
                            putString("showType", "personAllFilms")
                            putString("collection", "")
                            putInt("personId",fragmentPersonViewModel.personId)
                            putString("label", "All films by ${if (person.nameRu.isNullOrBlank()) person.nameEn else person.nameRu}")
                        }
                        findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
                    }

                    binding.imageViewPersonPoster.setOnClickListener {
                        StfalconImageViewer.Builder<String>(context, arrayOf(person.posterUrl)) {view , posterUrl->
                            Glide.with(binding.root).load(posterUrl).into(view)
                        }.show()
                    }

                }
            }
        }

        lifecycleScope.launch {
            fragmentPersonViewModel.loadingState.collect{
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


    private fun onFilmClicked(personFilm: PersonFilm) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", personFilm.filmId)
            putString("label", if (personFilm.nameRu.isNullOrBlank()) personFilm.nameEng else personFilm.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentFilm, bundle)

    }


    private suspend fun getFilmDetails(personFilm: PersonFilm): Film {
        return fragmentPersonViewModel.getFilmDetails(personFilm)
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
                    fragmentPersonViewModel.getPerson()
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
                    fragmentPersonViewModel.getPerson()
                }.show()
        }
    }
}