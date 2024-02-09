package igor.petrov.final_android_lvl1.data.dto.serialDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.serial.SerialEpisode

@JsonClass(generateAdapter = true)
data class SerialEpisodeDto(
    @Json(name = "seasonNumber") override val seasonNumber: Int,
    @Json(name = "episodeNumber") override val episodeNumber: Int,
    @Json(name = "nameRu") override val nameRu: String? = null,
    @Json(name = "nameEn") override val nameEn: String? = null,
    @Json(name = "synopsis") override val synopsis: String? = null,
    @Json(name = "releaseDate") override val releaseDate: String? = null
):SerialEpisode
