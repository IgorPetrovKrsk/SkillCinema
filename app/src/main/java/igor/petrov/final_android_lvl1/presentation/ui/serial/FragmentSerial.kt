package igor.petrov.final_android_lvl1.presentation.ui.serial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.FragmentSerialSeasonsBinding
import igor.petrov.final_android_lvl1.entity.serial.SerialSeason
import igor.petrov.final_android_lvl1.presentation.adapters.SerialEpisodeAdapter
import igor.petrov.final_android_lvl1.presentation.ui.film.FilmFragmentViewModel
import kotlinx.coroutines.launch

class FragmentSerial : Fragment() {

    private val filmFragmentViewModel: FilmFragmentViewModel by activityViewModels()
    private var _binding: FragmentSerialSeasonsBinding? = null
    private val binding get() = _binding!!

    private val scale = App.applicationContext().resources.displayMetrics.density

    private val serialEpisodeAdapter = SerialEpisodeAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentSerialSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            filmFragmentViewModel.serial.collect {
                it.seasonsList.forEach { serialSeason ->
                    binding.chipGroupSeasons.addChip(requireContext(), serialSeason)
                }
                (binding.chipGroupSeasons.children.first() as Chip).setChipBackgroundColorResource(R.color.chipChecked)
            }
        }

        binding.recyclerViewSerialSeason.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewSerialSeason.adapter = serialEpisodeAdapter
        serialEpisodeAdapter.setData(filmFragmentViewModel.serial.value.seasonsList[0].episodesList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun ChipGroup.addChip(context: Context, serialSeason: SerialSeason) {

        val newChip = Chip(context).apply {
            id = View.generateViewId()
            text = serialSeason.number.toString()
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            width = (70 * scale + 0.5f).toInt()
            textSize = 20f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setChipBackgroundColorResource(R.color.chipUnChecked)
            addView(this)
        }
        newChip.setOnClickListener {
            binding.chipGroupSeasons.children.forEach {
                (it as Chip).setChipBackgroundColorResource(R.color.chipUnChecked)
            }
            newChip.setChipBackgroundColorResource(R.color.chipChecked)
            serialEpisodeAdapter.setData(serialSeason.episodesList)
            binding.recyclerViewSerialSeason.layoutManager?.scrollToPosition(0)
        }
    }
}