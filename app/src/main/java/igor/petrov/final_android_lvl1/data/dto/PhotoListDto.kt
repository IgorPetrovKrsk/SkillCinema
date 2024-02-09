package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.PhotoList

@JsonClass(generateAdapter = true)
data class PhotoListDto(
    @Json(name = "total") override val total: Int = 0,
    @Json(name = "totalPages") override val totalPages: Int = 0,
    @Json(name = "items") override val items: List<PhotoDto>? = null
):PhotoList
