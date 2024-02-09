package igor.petrov.final_android_lvl1.presentation.ui.film.bottomFilmCollections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.DialogInputTextBinding
import igor.petrov.final_android_lvl1.databinding.FragmentBottomFilmCollectionsBinding
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.presentation.adapters.BottomCollectionAdapter
import igor.petrov.final_android_lvl1.presentation.ui.film.FilmFragmentViewModel
import kotlinx.coroutines.launch

class BottomFilmCollectionsFragment : BottomSheetDialogFragment() {

    private val filmFragmentViewModel : FilmFragmentViewModel by activityViewModels()
    private val bottomFilmCollectionsFragmentViewModel: BottomFilmCollectionsFragmentViewModel by activityViewModels()

    private var _binding: FragmentBottomFilmCollectionsBinding? = null
    private val binding get() = _binding!!
    private var bottomCollectionAdapter = BottomCollectionAdapter{collection: CollectionsDB -> onCollectionClicked(collection)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentBottomFilmCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            bottomFilmCollectionsFragmentViewModel.kinopoiskId = it.getInt("kinopoiskId")
        }

        bottomFilmCollectionsFragmentViewModel.getFilm()
        lifecycleScope.launch {
            bottomFilmCollectionsFragmentViewModel.film.collect { film ->
                if (film.kinopoiskId != 0) {
                    binding.textViewBottomFilmName.text = "${film.nameRu ?: film.nameEn ?: film.nameOriginal ?: "---"}"
                    binding.textViewBottomGenre.text = "${film.year}, ${film.genres?.joinToString()}"
                    binding.textViewBottomFilmRating.text = "${film.ratingKinopoisk ?: film.ratingImdb ?: ""}"
                    Glide.with(binding.root).load(film.posterUrl).into(binding.imageViewBottomFilmPoster)
                }
            }
        }

        bottomFilmCollectionsFragmentViewModel.getCollections()

        binding.recyclerViewBottomCollections.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewBottomCollections.adapter = bottomCollectionAdapter

        lifecycleScope.launch {
            bottomFilmCollectionsFragmentViewModel.collectionList.collect {
                bottomCollectionAdapter.setData(it.toMutableList(),bottomFilmCollectionsFragmentViewModel.filmCollections.toMutableList())
            }
        }


        binding.imageViewBottomClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBottomCreateCollection.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_create_collection))
                .setMessage(resources.getString(R.string.text_create_collection_input_name))
                .setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root)
                .setNegativeButton(resources.getString(R.string.text_cancel)) { dialog, which -> }
                .setPositiveButton(resources.getString(R.string.text_create_collection_accept)) { dialog, which ->
                    bottomFilmCollectionsFragmentViewModel.newCollection((dialog as AppCompatDialog).findViewById<EditText>(R.id.editTextCollectionName)?.text.toString(), bottomCollectionAdapter)
                }.show()
        }
    }


    private fun onCollectionClicked(collection: CollectionsDB) {
        bottomFilmCollectionsFragmentViewModel.addOrRemoveFilmFromCollection(collection)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        filmFragmentViewModel.getFilmCollectionsList() //чтобы обновились коллекции в film fragment
    }
}