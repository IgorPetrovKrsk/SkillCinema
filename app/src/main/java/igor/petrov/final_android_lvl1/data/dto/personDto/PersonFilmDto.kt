package igor.petrov.final_android_lvl1.data.dto.personDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.person.PersonFilm

@JsonClass(generateAdapter = true)
data class PersonFilmDto(
    @Json(name = "filmId") override val filmId: Int,
    @Json(name = "nameRu") override val nameRu: String? = null,
    @Json(name = "nameEng") override val nameEng: String? = null,
    @Json(name = "rating") override val rating: String? = null,
    @Json(name = "general") override val general: Boolean? = null,
    @Json(name = "description") override val description: String? = null,
    @Json(name = "professionKey") override val professionKey: String? = null,
    override val posterUrl: String? = null,
    override val posterUrlPreview: String? = null,
    override val ratingKinopoisk: Float? = null,
    override val ratingImdb: Float? = null
) : PersonFilm
