package igor.petrov.final_android_lvl1.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import igor.petrov.final_android_lvl1.data.dto.FilmDto

@Dao
interface FilmDao {

    @Query("Select * FROM FilmDB WHERE kinopoiskId = :kinopoiskId")
    fun getFilm(kinopoiskId:Int): FilmDto?

    @Upsert
    fun newFilm(newFilm: FilmDto)

    @Query ("DELETE FROM FilmDB WHERE kinopoiskId = :kinopoiskId")
    fun deleteFilm(kinopoiskId:Int)

    @Query ("DELETE FROM FilmDB")
    fun deleteAllFilms()
}