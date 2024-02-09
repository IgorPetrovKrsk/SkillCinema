package igor.petrov.final_android_lvl1.data.repository

import android.util.Log
import igor.petrov.final_android_lvl1.data.KinopoiskCollectionsRetrofit
import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.FilmListDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.data.dto.GenresCountriesListDto
import igor.petrov.final_android_lvl1.data.dto.PhotoListDto
import igor.petrov.final_android_lvl1.data.dto.StaffDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.data.dto.serialDto.SerialSeasonsListDto
import igor.petrov.final_android_lvl1.data.dto.similarFilmDto.SimilarFilmListDto
import retrofit2.Response
import javax.inject.Inject

class KinopoiskRepository @Inject constructor() {
    suspend fun getFilmPremieresList(): Response<FilmListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getFilmPremieresList()
    }

    suspend fun getFilmCollectionList(collectionName: String = "TOP_POPULAR_ALL", page: Int = 1): Response<FilmListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getFilmCollectionList(collectionName, page)
    }

    suspend fun getGenresCountriesList(): Response<GenresCountriesListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getGenresCountriesList()
    }

    suspend fun getFilmFilterList(page: Int = 1, countryDto: CountryDto?, genreDto: GenreDto?, order: String? = "RATING", type: String? = null, ratingFrom: Int = 0, ratingTo: Int = 10, yearFrom: Int = 1000, yearTo: Int = 3000, imdbId: String? = null, keyword: String? = null): Response<FilmListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getFilmFilterList(page, countryDto?.id, genreDto?.id, order ?: "RATING", type, ratingFrom, ratingTo, yearFrom, yearTo, imdbId, keyword)
    }

    suspend fun getFilm(kinopoiskId: Int): Response<FilmDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getFilm(kinopoiskId)
    }

    suspend fun getStaff(kinopoiskId: Int): Response<List<StaffDto>> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getStaff(kinopoiskId)
    }

    suspend fun getFilmPhotoList(page: Int = 1, kinopoiskId: Int, type: PhotoType?): Response<PhotoListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getFilmPhotoList(kinopoiskId, page, type)
    }

    suspend fun getSimilarFilmList(kinopoiskId: Int): Response<SimilarFilmListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getSimilarFilmList(kinopoiskId)
    }

    suspend fun getPerson(personId: Int): Response<PersonDto> {
        val result = KinopoiskCollectionsRetrofit.kinopoiskApi.getPerson(personId)
        Log.d("Retrofit2", "getPerson: " + result)
        return result
    }

    suspend fun getSerialSeasonsList(serialId: Int): Response<SerialSeasonsListDto> {
        return KinopoiskCollectionsRetrofit.kinopoiskApi.getSerialSeasonsList(serialId)
    }
}




