package igor.petrov.final_android_lvl1.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import igor.petrov.final_android_lvl1.data.dto.similarFilmDto.SimilarFilmDto
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm

class DiffUtilSimilarFilmCallback : DiffUtil.ItemCallback<SimilarFilm>(){
    override fun areItemsTheSame(oldItem: SimilarFilm, newItem: SimilarFilm): Boolean = oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: SimilarFilm, newItem: SimilarFilm): Boolean = oldItem as SimilarFilmDto == newItem as SimilarFilmDto
}