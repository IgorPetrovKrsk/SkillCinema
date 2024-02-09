package igor.petrov.final_android_lvl1.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.RecyclerViewFilmViewHolderBinding
import igor.petrov.final_android_lvl1.entity.Film
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmAdapter(private val onFilmClicked: (Film) -> Unit, private val isFilmViewed  : suspend (Film) -> Boolean) : PagingDataAdapter<Film, PagedFilmViewHolder>(DiffUtilFilmCallback()) {

    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val adapterScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedFilmViewHolder {
        val binding = RecyclerViewFilmViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return PagedFilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagedFilmViewHolder, position: Int) {
        val film = getItem(position)
        if (film != null) {
            holder.binding.textViewFilmName.text = "${film.nameRu ?: ""} ${film.nameEn ?: ""} ${film.nameOriginal ?: ""}"
            holder.binding.textViewFilmGenre.text = film.genres?.joinToString()
            dbScope.launch {
                val isFilmViewed = isFilmViewed(film)
                adapterScope.launch {
                    holder.binding.imageViewFilmViewed.visibility = if (isFilmViewed) View.VISIBLE else View.GONE
                }
            }
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



