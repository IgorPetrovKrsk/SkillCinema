package igor.petrov.final_android_lvl1

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalStdlibApi::class)
class ConvertersTest {

    private val testCountryString = "[{\"id\":1,\"country\":\"USA\"}]"
    private val moshi: Moshi = Moshi.Builder().build()
    private val jsonAdapter: JsonAdapter<List<CountryDto>> = moshi.adapter<List<CountryDto>>()
    private val testCountryList = listOf<CountryDto>(CountryDto(1,"USA"))

    @Test
    fun fromCountryListTest() {
        val countryString = jsonAdapter.toJson(testCountryList)
        assertEquals(testCountryString,countryString)
    }

    @Test
    fun toCountryListTest() {
        val countryList = jsonAdapter.fromJson(testCountryString)
        assertEquals(countryList,testCountryList)
    }
}