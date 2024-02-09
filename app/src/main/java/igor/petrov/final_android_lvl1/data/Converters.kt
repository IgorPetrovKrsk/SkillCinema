package igor.petrov.final_android_lvl1.data

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonFilmDto
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class, ExperimentalStdlibApi::class)
class Converters {

    @TypeConverter
    fun fromCountryList(list: List<CountryDto>):String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<CountryDto>> = moshi.adapter<List<CountryDto>>()
        return jsonAdapter.toJson(list)
    }

    @TypeConverter
    fun toCountryList(value: String):List<CountryDto>?{
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<CountryDto>> = moshi.adapter<List<CountryDto>>()
        return jsonAdapter.fromJson(value)
    }

    @TypeConverter
    fun fromGenreList(list: List<GenreDto>):String{
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<GenreDto>> = moshi.adapter<List<GenreDto>>()
        return jsonAdapter.toJson(list)
    }

    @TypeConverter
    fun toGenreList(value: String):List<GenreDto>?{
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<GenreDto>> = moshi.adapter<List<GenreDto>>()
        return jsonAdapter.fromJson(value)
    }

    @TypeConverter
    fun fromPersonFilmList(list: List<PersonFilmDto>): String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<PersonFilmDto>> = moshi.adapter<List<PersonFilmDto>>()
        return jsonAdapter.toJson(list)
    }

    @TypeConverter
    fun toPersonFilmListList(value: String):List<PersonFilmDto>?{
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<PersonFilmDto>> = moshi.adapter<List<PersonFilmDto>>()
        return jsonAdapter.fromJson(value)
    }

}