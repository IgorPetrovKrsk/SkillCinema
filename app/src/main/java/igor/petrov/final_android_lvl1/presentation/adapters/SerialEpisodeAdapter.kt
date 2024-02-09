package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import igor.petrov.final_android_lvl1.databinding.RecyclerViewSerialEpisodeViewHolderBinding
import igor.petrov.final_android_lvl1.entity.serial.SerialEpisode

class SerialEpisodeAdapter():Adapter<SerialEpisodeViewHolder>() {

    private var data: List<SerialEpisode> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<SerialEpisode>?) {
        this.data = data ?: emptyList()
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerialEpisodeViewHolder {
        val binding = RecyclerViewSerialEpisodeViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return SerialEpisodeViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SerialEpisodeViewHolder, position: Int) {
        val episode = data.getOrNull(position)
        if (episode!=null){
            holder.binding.textViewEpisodeNumberName.text = "S${episode.seasonNumber}.E${episode.episodeNumber} - ${if (episode.nameEn.isNullOrBlank()) episode.nameRu else episode.nameEn}"
            holder.binding.textViewEpisodeDescription.text = "${episode.synopsis?:"---"}"
            holder.binding.textViewEpisodeDate.text = "Release date: ${episode.releaseDate?:""}"
        }
    }

}

