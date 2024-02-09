package igor.petrov.final_android_lvl1.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.Staff

@JsonClass(generateAdapter = true)
data class StaffDto(
    @Json(name = "staffId") override val staffId: Int,
    @Json(name = "nameRu") override val nameRu: String? = null,
    @Json(name = "nameEn") override val nameEn: String? = null,
    @Json(name = "description") override val description: String? = null,
    @Json(name = "posterUrl") override val posterUrl: String? = null,
    @Json(name = "professionText") override val professionText: String? = null,
    @Json(name = "professionKey") override val professionKey: String? = null,
) : Staff
