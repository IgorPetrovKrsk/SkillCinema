package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.Genre

@JsonClass(generateAdapter = true)
class GenreDto(
    @Json(name = "id") override val id: Int?,
    @Json(name = "genre") override val genre: String
) : Genre{
    override fun toString(): String {
        return genre
    }
}