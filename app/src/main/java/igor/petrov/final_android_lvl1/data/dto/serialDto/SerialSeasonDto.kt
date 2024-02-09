package igor.petrov.final_android_lvl1.data.dto.serialDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.serial.SerialSeason

@JsonClass(generateAdapter = true)
data class SerialSeasonDto(
    @Json(name = "number") override val number: Int,
    @Json(name = "episodes") override val episodesList: List<SerialEpisodeDto>
): SerialSeason





