package igor.petrov.final_android_lvl1.presentation.ui.photo

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
import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.FragmentPhotoBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.Photo
import igor.petrov.final_android_lvl1.presentation.adapters.GalleryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FragmentPhoto : Fragment() {

    private val fragmentPhotoViewModel: FragmentPhotoViewModel by activityViewModels()
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private val fragmentScope = CoroutineScope(Dispatchers.Main)

    private val galleryAdapter = GalleryAdapter { photo: Photo, position:Int -> onPhotoClicked(photo,position) }
    private val scale = App.applicationContext().resources.displayMetrics.density

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            fragmentPhotoViewModel.kinopoiskId = it.getInt("kinopoiskId")
        }
        fragmentPhotoViewModel.getPhotoList()

        PhotoType.entries.forEach {
            binding.chipGroupPhoto.addChip(requireContext(), it)
        }

        val selectedChip = binding.chipGroupPhoto.children.find { (it as Chip).text == fragmentPhotoViewModel.photoType.label} as Chip
        selectedChip.setChipBackgroundColorResource(R.color.chipChecked)

        //(binding.chipGroupPhoto.children.first() as Chip).setChipBackgroundColorResource(R.color.chipChecked)

        binding.recyclerViewPhoto.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewPhoto.adapter = galleryAdapter

        fragmentScope.launch {
            fragmentPhotoViewModel.photoList.collect {
                galleryAdapter.submitData(it)
            }
        }

        galleryAdapter.loadStateFlow.onEach {
            when (it.refresh) {
                is LoadState.Error -> {
                    activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
                    handleError((it.refresh as LoadState.Error).error)
                }

                LoadState.Loading -> activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.VISIBLE
                is LoadState.NotLoading -> activity?.findViewById<View>(R.id.progressBarLoading)?.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)
    }


//        binding.recyclerViewSerialSeason.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.recyclerViewSerialSeason.adapter = serialEpisodeAdapter
//        serialEpisodeAdapter.setData(fragmentFilmViewModel.serial.value.seasonsList[0].episodesList)


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //fragmentScope.cancel()
    }

    private fun ChipGroup.addChip(context: Context, photoType: PhotoType) {

        val newChip = Chip(context).apply {
            id = View.generateViewId()
            text = photoType.label
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            //width = (70 * scale + 0.5f).toInt()
            textSize = 20f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setChipBackgroundColorResource(R.color.chipUnChecked)
            addView(this)
        }
        newChip.setOnClickListener {
            binding.chipGroupPhoto.children.forEach {
                (it as Chip).setChipBackgroundColorResource(R.color.chipUnChecked)
            }
            newChip.setChipBackgroundColorResource(R.color.chipChecked)
            fragmentPhotoViewModel.photoType = photoType
            galleryAdapter.refresh()
            binding.recyclerViewPhoto.layoutManager?.scrollToPosition(0)
        }
    }

    private fun onPhotoClicked(photo: Photo, position:Int) {
        val bundle = Bundle().apply {
            putString("photoFrom","fromPhoto")
            putString("photoUrl",photo.imageUrl)
            putInt("photoPosition",position)
        }
        findNavController().navigate(R.id.action_global_fragmentFullScreenPhoto,bundle)
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
                    fragmentPhotoViewModel.getPhotoList()
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
                    fragmentPhotoViewModel.getPhotoList()
                }.show()
        }
    }
}