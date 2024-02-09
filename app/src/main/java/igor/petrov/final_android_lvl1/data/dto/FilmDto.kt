package igor.petrov.final_android_lvl1.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.entity.Film

@JsonClass(generateAdapter = true)
@Entity(tableName = "FilmDB")
data class FilmDto(
    @PrimaryKey
    @ColumnInfo(name = "kinopoiskId")
    @Json(name = "kinopoiskId") override val kinopoiskId: Int,
    @ColumnInfo(name = "nameRu")
    @Json(name = "nameRu") override val nameRu: String? = null,
    @ColumnInfo(name = "nameEn")
    @Json(name = "nameEn") override val nameEn: String? = null,
    @ColumnInfo(name = "nameOriginal")
    @Json(name = "nameOriginal") override val nameOriginal: String? = null,
    @ColumnInfo(name = "year")
    @Json(name = "year") override val year: Int? = null,
    @ColumnInfo(name = "posterUrl")
    @Json(name = "posterUrl") override val posterUrl: String? = null,
    @ColumnInfo(name = "posterUrlPreview")
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String? = null,
    @ColumnInfo(name = "countries")
    @Json(name = "countries") override val countries: List<CountryDto>? = null,
    @ColumnInfo(name = "genres")
    @Json(name = "genres") override val genres: List<GenreDto>? = null,
    @ColumnInfo(name = "duration")
    @Json(name = "duration") override val duration: Int? = null,
    @ColumnInfo(name = "premiereRu")
    @Json(name = "premiereRu") override val premiereRu: String? = null,
    @ColumnInfo(name = "FilmType")
    @Json(name = "type") override val type: FilmType? = null,
    @ColumnInfo(name = "kinopoiskHDId")
    @Json(name = "kinopoiskHDId") override val kinopoiskHDId: String? = null,
    @ColumnInfo(name = "imdbId")
    @Json(name = "imdbId") override val imdbId: String? = null,
    @ColumnInfo(name = "coverUrl")
    @Json(name = "coverUrl") override val coverUrl: String? = null,
    @ColumnInfo(name = "logoUrl")
    @Json(name = "logoUrl") override val logoUrl: String? = null,
    @ColumnInfo(name = "ratingKinopoisk")
    @Json(name = "ratingKinopoisk") override val ratingKinopoisk: Float? = null,
    @ColumnInfo(name = "ratingImdb")
    @Json(name = "ratingImdb") override val ratingImdb: Float? = null,
    @ColumnInfo(name = "webUrl")
    @Json(name = "webUrl") override val webUrl: String? = null,
    @ColumnInfo(name = "slogan")
    @Json(name = "slogan") override val slogan: String? = null,
    @ColumnInfo(name = "description")
    @Json(name = "description") override val description: String? = null,
    @ColumnInfo(name = "shortDescription")
    @Json(name = "shortDescription") override val shortDescription: String? = null,
    @ColumnInfo(name = "serial")
    @Json(name = "serial") override val serial: Boolean? = null,
    @ColumnInfo(name = "shortFilm")
    @Json(name = "shortFilm") override val shortFilm: Boolean? = null,
    @ColumnInfo(name = "completed")
    @Json(name = "completed") override val completed: Boolean? = null,
    @ColumnInfo(name = "ratingMpaa")
    @Json(name = "ratingMpaa") override val ratingMpaa: String? = null,
    @ColumnInfo(name = "ratingAgeLimits")
    @Json(name = "ratingAgeLimits") override val ratingAgeLimits: String? = null,
    @ColumnInfo(name = "timeUpdated")
    val timeUpdated: Long? = System.currentTimeMillis()
) : Film
