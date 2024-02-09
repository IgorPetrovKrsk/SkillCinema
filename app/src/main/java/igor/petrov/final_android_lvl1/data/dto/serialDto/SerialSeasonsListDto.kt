package igor.petrov.final_android_lvl1.data.dto.serialDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.serial.SerialSeasonsList

@JsonClass(generateAdapter = true)
class SerialSeasonsListDto(
    @Json(name = "total") override val total: Int,
    @Json(name = "items") override val seasonsList: List<SerialSeasonDto>
) : SerialSeasonsList