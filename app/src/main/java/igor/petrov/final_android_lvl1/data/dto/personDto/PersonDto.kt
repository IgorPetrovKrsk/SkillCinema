package igor.petrov.final_android_lvl1.data.dto.personDto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import igor.petrov.final_android_lvl1.entity.person.Person

@JsonClass(generateAdapter = true)
@Entity(tableName = "PersonDB")
data class PersonDto(
    @PrimaryKey
    @ColumnInfo(name = "personId")
    @Json(name = "personId") override val personId: Int,
    @ColumnInfo(name = "nameRu")
    @Json(name = "nameRu") override val nameRu: String? = null,
    @ColumnInfo(name = "nameEn")
    @Json(name = "nameEn") override val nameEn: String? = null,
    @ColumnInfo(name = "posterUrl")
    @Json(name = "posterUrl") override val posterUrl: String? = null,
    @ColumnInfo(name = "webUrl")
    @Json(name = "webUrl") override val webUrl: String? = null,
    @ColumnInfo(name = "sex")
    @Json(name = "sex") override val sex: String? = null,
    @ColumnInfo(name = "growth")
    @Json(name = "growth") override val growth: Int? = null,
    @ColumnInfo(name = "birthday")
    @Json(name = "birthday") override val birthday: String? = null,
    @ColumnInfo(name = "death")
    @Json(name = "death") override val death: String? = null,
    @ColumnInfo(name = "age")
    @Json(name = "age") override val age: Int? = null,
    @ColumnInfo(name = "birthplace")
    @Json(name = "birthplace") override val birthplace: String? = null,
    @ColumnInfo(name = "deathplace")
    @Json(name = "deathplace") override val deathplace: String? = null,
    //@ColumnInfo(name = "spouses")
    //@Json(name = "spouses") override val spouses: List<PersonDto>? = null,
    @ColumnInfo(name = "hasAwards")
    @Json(name = "hasAwards") override val hasAwards: Int? = null,
    @ColumnInfo(name = "profession")
    @Json(name = "profession") override val profession: String? = null,
    @ColumnInfo(name = "films")
    @Json(name = "films") override val films: List<PersonFilmDto>? = null,
    @ColumnInfo(name = "timeUpdated")
    val timeUpdated: Long? = System.currentTimeMillis()
) : Person
