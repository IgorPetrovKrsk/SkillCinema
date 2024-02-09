package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.databinding.RecyclerViewFilmViewHolderBinding
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.person.PersonFilm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonFilmAdapter(private val onFilmClicked: (PersonFilm) -> Unit, private val getFilmDetails: suspend (PersonFilm) -> Film) : Adapter<PersonFilmViewHolder>() {

    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val adapterScope = CoroutineScope(Dispatchers.Main)
    private var data: List<PersonFilm> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<PersonFilm>?) {
        this.data = data ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonFilmViewHolder {
        val binding = RecyclerViewFilmViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return PersonFilmViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PersonFilmViewHolder, position: Int) {
        val personFilm = data.getOrNull(position)
        if (personFilm != null) {
            holder.binding.textViewFilmName.text = if (personFilm.nameRu.isNullOrBlank()) personFilm.nameEng else personFilm.nameRu
            holder.binding.imageViewFilmPoster.setOnClickListener {
                onFilmClicked(personFilm)
            }
            dbScope.launch {
                val film = getFilmDetails(personFilm)
                adapterScope.launch {
                    holder.binding.textViewFilmGenre.text = film.genres?.joinToString()
                    holder.binding.textViewFilmRating.text = "${film.ratingKinopoisk ?: film.ratingImdb ?: ""}"
                    Glide.with(holder.binding.root).load(film.posterUrlPreview).into(holder.binding.imageViewFilmPoster)
                }
            }

        }
    }

}

