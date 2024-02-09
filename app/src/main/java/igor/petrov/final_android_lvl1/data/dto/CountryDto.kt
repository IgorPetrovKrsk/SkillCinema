package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.Country

@JsonClass(generateAdapter = true)
class CountryDto(
    @Json(name = "id") override val id: Int?,
    @Json(name = "country") override val country: String
) : Country {
}