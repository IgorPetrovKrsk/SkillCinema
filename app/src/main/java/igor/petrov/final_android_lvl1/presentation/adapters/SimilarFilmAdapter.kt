package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.RecyclerViewFilmViewHolderBinding
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm

class SimilarFilmAdapter(private val onFilmClicked: (SimilarFilm) -> Unit) : RecyclerView.Adapter<PagedFilmViewHolder>() {

    private var data: List<SimilarFilm> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<SimilarFilm>?) {
        this.data = (data ?: emptyList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedFilmViewHolder {
        val binding = RecyclerViewFilmViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return PagedFilmViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PagedFilmViewHolder, position: Int) {
        val film = data.getOrNull(position)
        if (film != null) {
            holder.binding.textViewFilmName.text = "${film.nameRu ?: ""} ${film.nameEn ?: ""} ${film.nameOriginal ?: ""}"
            holder.binding.textViewFilmGenre.visibility = View.GONE
            holder.binding.textViewFilmRating.text = ""
            when (film.nameRu) {
                "Show all" -> {
                    Glide.with(holder.binding.root).load(R.drawable.ic_right_arrow).into(holder.binding.imageViewFilmPoster)
                }

                "No films" -> {
                    Glide.with(holder.binding.root).load(R.drawable.ic_refresh).into(holder.binding.imageViewFilmPoster)
                }

                else -> {
                    Glide.with(holder.binding.root).load(film.posterUrlPreview).into(holder.binding.imageViewFilmPoster)
                }
            }
            holder.binding.imageViewFilmPoster.setOnClickListener {
                onFilmClicked(film)
            }

        }
    }
}



