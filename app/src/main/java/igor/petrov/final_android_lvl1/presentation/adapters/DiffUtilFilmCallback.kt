package igor.petrov.final_android_lvl1.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.entity.Film

class DiffUtilFilmCallback : DiffUtil.ItemCallback<Film>(){
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem as FilmDto == newItem as FilmDto
}