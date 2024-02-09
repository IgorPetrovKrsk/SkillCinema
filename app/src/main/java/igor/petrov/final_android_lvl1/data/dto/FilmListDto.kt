package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.FilmList

@JsonClass(generateAdapter = true)
class FilmListDto(
    @Json(name = "total") override val totalFilms: Int,
    @Json(name = "items") override val filmList: List<FilmDto>
) : FilmList