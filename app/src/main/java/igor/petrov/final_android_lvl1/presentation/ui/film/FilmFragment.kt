package igor.petrov.final_android_lvl1.presentation.ui.film

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentFilmBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.Photo
import igor.petrov.final_android_lvl1.entity.Staff
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm
import igor.petrov.final_android_lvl1.presentation.adapters.GalleryAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.SimilarFilmAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.StaffAdapter
import kotlinx.coroutines.launch


class FilmFragment : Fragment() {

    private val filmFragmentViewModel: FilmFragmentViewModel by activityViewModels()
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilmBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            filmFragmentViewModel.kinopoiskId = it.getInt("kinopoiskId")
            filmFragmentViewModel.label = it.getString("label")
        }

        filmFragmentViewModel.getFilm()
        filmFragmentViewModel.getStaff()
        filmFragmentViewModel.getGallery()
        filmFragmentViewModel.getSimilarList()
        filmFragmentViewModel.getFilmCollectionsList()

        val actorsAdapter = StaffAdapter { staff: Staff -> onStaffClicked(staff) }

        binding.recyclerViewFilmCast.layoutManager = GridLayoutManager(context, 5)
        binding.recyclerViewFilmCast.adapter = actorsAdapter

        lifecycleScope.launch {
            filmFragmentViewModel.actorsList.collect {list->
                actorsAdapter.setData(list.take(20))
                binding.btnShowAllCast.text = "${list.size} >"

                binding.btnShowAllCast.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("showType", "filmCrew")
                        putInt("kinopoiskId", filmFragmentViewModel.kinopoiskId)
                        putString("collection", "filmActors")
                        putString("label", "Actors of ${filmFragmentViewModel.label}")
                        putString("json", filmFragmentViewModel.getStaffToJson(list))
                    }
                    findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
                }
            }
        }

        val crewAdapter = StaffAdapter { staff: Staff -> onStaffClicked(staff) }

        binding.recyclerViewFilmCrew.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewFilmCrew.adapter = crewAdapter

        lifecycleScope.launch {
            filmFragmentViewModel.crewList.collect {list ->
                crewAdapter.setData(list.take(6))
                binding.btnShowAllCrew.text = "${list.size} >"

                binding.btnShowAllCrew.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("showType", "filmCrew")
                        putInt("kinopoiskId", filmFragmentViewModel.kinopoiskId)
                        putString("collection", "filmActors")
                        putString("label", "Crew of ${filmFragmentViewModel.label}")
                        putString("json", filmFragmentViewModel.getStaffToJson(list))
                    }
                    findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
                }
            }
        }

        val galleryAdapter = GalleryAdapter { photo: Photo, position: Int -> onPhotoClicked(photo, position) }

        binding.recyclerViewFilmGallery.layoutManager = LinearLayoutManager(App.applicationContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmGallery.adapter = galleryAdapter

        lifecycleScope.launch {
            filmFragmentViewModel.galleryList.collect {
                galleryAdapter.submitData(it)
            }
        }

        binding.btnShowAllGallery.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("kinopoiskId", filmFragmentViewModel.kinopoiskId)
                putString("label", "Gallery of ${filmFragmentViewModel.label}")
            }
            findNavController().navigate(R.id.action_global_fragmentPhoto, bundle)
        }

        val similarFilmAdapter = SimilarFilmAdapter { similarFilm: SimilarFilm -> onFilmClicked(similarFilm) }
        binding.recyclerViewFilmSimilar.layoutManager = LinearLayoutManager(App.applicationContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmSimilar.adapter = similarFilmAdapter
        lifecycleScope.launch {
            filmFragmentViewModel.similarFilmList.collect {similarFilmList ->
                similarFilmAdapter.setData(similarFilmList)
                binding.btnShowAllSimilar.text = "${similarFilmList.size} >"
                binding.btnShowAllSimilar.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("showType", "similarFilm")
                        putInt("kinopoiskId", filmFragmentViewModel.kinopoiskId)
                        putString("collection", "similarFilm")
                        putString("label", "Films similar to ${filmFragmentViewModel.label}")
                        putString("json", filmFragmentViewModel.getSimilarFilmToJson(similarFilmList))
                    }
                    findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
                }
            }

        }

        lifecycleScope.launch {
            filmFragmentViewModel.film.collect { film ->
                if (film.kinopoiskId != 0) {
                    binding.textViewFilmRatingName.text = "${film.ratingKinopoisk ?: film.ratingImdb ?: ""} ${film.nameRu ?: film.nameEn ?: film.nameOriginal ?: "---"}"
                    binding.textViewFilmYearGenreEtc.text = "${film.year} ${film.genres?.joinToString()} ${film.countries?.firstOrNull()?.country} ${film.duration ?: ""} ${film.ratingMpaa ?: ""}"
                    binding.textViewFilmShortDescription.text = "${film.nameRu ?: film.nameEn ?: film.nameOriginal ?: "---"}"
                    binding.textViewFilmDescription.text = "${film.description}"
                    Glide.with(binding.root).load(film.posterUrl).into(binding.imageViewFilmPoster)
                    Glide.with(binding.root).load(film.logoUrl).into(binding.imageViewFilmLogo)
                    if (film.serial == false) {
                        binding.textViewSerialSeasonsEpisodes.visibility = View.GONE
                        binding.textViewSerialSeasonsEpisodesText.visibility = View.GONE
                        binding.btnShowAllSerialSeasons.visibility = View.GONE
                    } else {
                        filmFragmentViewModel.getSerialSeasons()
                        lifecycleScope.launch {
                            filmFragmentViewModel.serial.collect { serialSeasonList ->
                                if (serialSeasonList.total != 0) {
                                    binding.textViewSerialSeasonsEpisodesText.text = "${serialSeasonList.total} seasons, ${serialSeasonList.seasonsList.sumOf { it.episodesList.size }} episodes"
                                }
                            }
                        }
                        binding.btnShowAllSerialSeasons.setOnClickListener {
                            onShowAllSerialSeasonsClicked(film)
                        }
                    }


                    binding.imageViewFilmShare.setOnClickListener {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, if (film.imdbId != null) "https://www.imdb.com/title/${film.imdbId}" else film.webUrl)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }
                }
            }
        }

        lifecycleScope.launch {
            filmFragmentViewModel.collectionsList.collect {
                //setCollectionsIcons(it)
                if (it.any { it.collectionId == 1 }) {
                    binding.imageViewFilmFavorites.setImageResource(R.drawable.ic_heart_off)
                } else {
                    binding.imageViewFilmFavorites.setImageResource(R.drawable.ic_heart)
                }
                if (it.any { it.collectionId == 2 }) {
                    binding.imageViewFilmViewLater.setImageResource(R.drawable.ic_bookmark_off)
                } else {
                    binding.imageViewFilmViewLater.setImageResource(R.drawable.ic_bookmark)
                }
                if (it.any { it.collectionId == 3 }) {
                    binding.imageViewViewed.setImageResource(R.drawable.ic_eye_off)
                } else {
                    binding.imageViewViewed.setImageResource(R.drawable.ic_eye)
                }
            }
        }

        binding.imageViewFilmFavorites.setOnClickListener {
            filmFragmentViewModel.addOrRemoveFilmFromCollection(CollectionsDBDto("Favorites", true, 1))
        }
        binding.imageViewFilmViewLater.setOnClickListener {
            filmFragmentViewModel.addOrRemoveFilmFromCollection(CollectionsDBDto("View later", true, 2))
        }
        binding.imageViewViewed.setOnClickListener {
            filmFragmentViewModel.addOrRemoveFilmFromCollection(CollectionsDBDto("Viewed", true, 3))
        }

        binding.imageViewFilmCollections.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("kinopoiskId", filmFragmentViewModel.kinopoiskId)
                putString("label", requireContext().getString(R.string.text_add_to_collection))
            }
            findNavController().navigate(R.id.action_fragmentFilm_to_bottomFilmCollectionsFragment, bundle)
        }


        binding.textViewFilmDescription.setOnClickListener {

            if (binding.textViewFilmDescription.maxLines == 3) {
                binding.textViewFilmDescription.maxLines = 20

            } else {
                binding.textViewFilmDescription.maxLines = 3
            }
            val transition = LayoutTransition()
            transition.setDuration(300)
            transition.enableTransitionType(LayoutTransition.CHANGING)
            binding.constraintLayoutFilm.layoutTransition = transition
        }

        lifecycleScope.launch {
            filmFragmentViewModel.loadingState.collect{
                when (it) {
                    is LoadingState.Error -> {
                        binding.progressBarFilmLoading.visibility = View.GONE
                        handleError(it.kinopoiskException)
                    }
                    LoadingState.Loading -> {
                        binding.progressBarFilmLoading.visibility = View.VISIBLE
                        binding.imageViewFilmPosterLoading.visibility = View.VISIBLE
                        binding.imageViewFilmPoster.visibility = View.INVISIBLE
                        binding.textViewFilmRatingName.text = resources.getText(R.string.text_loading)
                        binding.textViewFilmYearGenreEtc.text = resources.getText(R.string.text_loading)
                        binding.textViewFilmShortDescription.text = resources.getText(R.string.text_loading)
                        binding.textViewFilmDescription.text = resources.getText(R.string.text_loading)
                    }
                    LoadingState.Ready -> {
                        binding.progressBarFilmLoading.visibility = View.GONE
                        binding.imageViewFilmPosterLoading.visibility = View.INVISIBLE
                        binding.imageViewFilmPoster.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.progressBarFilmLoading.visibility = View.GONE
                    }
                }
            }
        }




    }

    private fun onShowAllSerialSeasonsClicked(film: Film) {
        val bundle = Bundle().apply {
            putInt("serialId", film.kinopoiskId)
            putString("label", if (film.nameRu.isNullOrBlank()) film.nameEn else film.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentSerial, bundle)
    }

    private fun onPhotoClicked(photo: Photo, position: Int) {
//        StfalconImageViewer.Builder<Photo>(requireContext(), arrayOf( photo)) { view, image ->
//            Glide.with(requireContext()).load(image.imageUrl).into(view)
//        }.show()
        val bundle = Bundle().apply {
            putString("photoFrom", "fromFilm")
            putString("photoUrl", photo.imageUrl)
            putInt("photoPosition", position)
        }
        findNavController().navigate(R.id.action_global_fragmentFullScreenPhoto, bundle)
    }

    private fun onStaffClicked(staff: Staff) {

        val bundle = Bundle().apply {
            putInt("personId", staff.staffId)
            putString("label", if (staff.nameRu.isNullOrBlank()) staff.nameEn else staff.nameRu)
        }
        findNavController().navigate(R.id.action_global_fragmentPerson, bundle)
    }

    private fun onFilmClicked(similarFilm: SimilarFilm) {
        val bundle = Bundle().apply {
            putInt("kinopoiskId", similarFilm.filmId)
            putString("label", similarFilm.nameRu)
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
                    filmFragmentViewModel.getFilm()
                    filmFragmentViewModel.getStaff()
                    filmFragmentViewModel.getGallery()
                    filmFragmentViewModel.getSimilarList()
                    filmFragmentViewModel.getFilmCollectionsList()
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
                    filmFragmentViewModel.getFilm()
                    filmFragmentViewModel.getStaff()
                    filmFragmentViewModel.getGallery()
                    filmFragmentViewModel.getSimilarList()
                    filmFragmentViewModel.getFilmCollectionsList()
                }.show()
        }
    }

}