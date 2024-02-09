package igor.petrov.final_android_lvl1.data.dto.similarFilmDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm

@JsonClass(generateAdapter = true)
data class SimilarFilmDto(
    @Json(name = "filmId") override val filmId: Int,
    @Json(name = "nameRu") override val nameRu: String? = null,
    @Json(name = "nameEn") override val nameEn: String? = null,
    @Json(name = "nameOriginal") override val nameOriginal: String? = null,
    @Json(name = "posterUrl") override val posterUrl: String? = null,
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String? = null,
    @Json(name = "relationType") override val relationType: String? = null
) : SimilarFilm
