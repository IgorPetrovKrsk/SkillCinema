package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.GenresCountriesList

@JsonClass(generateAdapter = true)
class GenresCountriesListDto (
     @Json(name = "genres") override val genresList: List<GenreDto>,
     @Json(name = "countries") override val countriesList: List<CountryDto>
) : GenresCountriesList
