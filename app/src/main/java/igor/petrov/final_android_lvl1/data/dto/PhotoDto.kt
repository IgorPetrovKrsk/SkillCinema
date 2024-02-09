package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoDto(
    @Json(name = "imageUrl") override val imageUrl: String?,
    @Json(name = "previewUrl") override val previewUrl: String?
) : igor.petrov.final_android_lvl1.entity.Photo
