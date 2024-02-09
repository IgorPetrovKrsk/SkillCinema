package igor.petrov.final_android_lvl1.data.api

import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.FilmListDto
import igor.petrov.final_android_lvl1.data.dto.GenresCountriesListDto
import igor.petrov.final_android_lvl1.data.dto.PhotoListDto
import igor.petrov.final_android_lvl1.data.dto.StaffDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.data.dto.serialDto.SerialSeasonsListDto
import igor.petrov.final_android_lvl1.data.dto.similarFilmDto.SimilarFilmListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Month
import java.util.Calendar

interface KinopoiskApi {
    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/premieres")
    suspend fun getFilmPremieresList(
        //@Query("page") page: Int = 1,
        @Query("year") year: Int = Calendar.getInstance().get(Calendar.YEAR), @Query("month") month: String = Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1).name): Response<FilmListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/collections")
    suspend fun getFilmCollectionList(@Query("type") type: String = "TOP_POPULAR_ALL", @Query("page") page: Int = 1): Response<FilmListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/filters")
    suspend fun getGenresCountriesList(): Response<GenresCountriesListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films")
    suspend fun getFilmFilterList(@Query("page") page: Int = 1, @Query("countries") countries: Int? = null, @Query("genres") genres: Int? = null, @Query("order") order: String = "RATING", @Query("type") type: String? = null, @Query("ratingFrom") ratingFrom: Int = 0, @Query("ratingTo") ratingTo: Int = 10, @Query("yearFrom") yearFrom: Int = 1000, @Query("yearTo") yearTo: Int = 3000, @Query("imdbId") imdbId: String? = null, @Query("keyword") keyword: String? = null): Response<FilmListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/{id}")
    suspend fun getFilm(@Path("id") id: Int): Response<FilmDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v1/staff")
    suspend fun getStaff(@Query("filmId") id: Int): Response<List<StaffDto>>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/{id}/images")
    suspend fun getFilmPhotoList(@Path("id") id: Int, @Query("page") page: Int = 1, @Query("type") type: PhotoType? = null): Response<PhotoListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/{id}/similars")
    suspend fun getSimilarFilmList(@Path("id") id: Int): Response<SimilarFilmListDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v1/staff/{id}")
    suspend fun getPerson(@Path("id") id: Int): Response<PersonDto>

    @Headers("X-API-KEY: $kinopoisk_api_key", "'Content-Type': 'application/json'")
    @GET("v2.2/films/{id}/seasons")
    suspend fun getSerialSeasonsList(@Path("id") id: Int): Response<SerialSeasonsListDto>

    companion object {
        private const val kinopoisk_api_key = "e1eb1015-aa86-4d62-be8e-cb8730a89156"
        //private const val kinopoisk_api_key = "5eb205f4-3df7-41d0-95ca-7ae6b4af9684" //второй для тестов
        //BASE_URL = https://kinopoiskapiunofficial.tech/api/
    }
}