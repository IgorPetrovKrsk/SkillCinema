package igor.petrov.final_android_lvl1.data.dto.similarFilmDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilmList

@JsonClass(generateAdapter = true)
class SimilarFilmListDto(
    @Json(name = "total") override val totalFilms: Int,
    @Json(name = "items") override val filmList: List<SimilarFilmDto>
) : SimilarFilmList