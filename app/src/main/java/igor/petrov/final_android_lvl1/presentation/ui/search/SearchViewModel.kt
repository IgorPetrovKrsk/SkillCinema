package igor.petrov.final_android_lvl1.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.presentation.ui.home.FilmFilterListPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {

    var filmFilterList: Flow<PagingData<Film>> = emptyFlow()

    var country = CountryDto(1,"США")
    var genre = GenreDto(1,"триллер")
    var keyWord: String? = null
    var order = OrderType.RATING
    var filmType = FilmType.ALL
    var ratingFrom = 0
    var ratingTo = 10
    var yearFrom = 1000
    var yearTo = 3000
    var imdbId = null
    var showOnlyViewed = false

    fun getFilmSearchResultList() {
        filmFilterList = Pager(config = PagingConfig(pageSize = 20),
            initialKey = null,
            pagingSourceFactory = {
                FilmFilterListPagingSource(
                    kinopoiskUseCase, country, genre, order, filmType, ratingFrom, ratingTo, yearFrom, yearTo, imdbId, keyWord, true
                )
            }).flow.cachedIn(viewModelScope)
    }

    suspend fun isFilmViewed(film: Film):Boolean{
        return kinopoiskUseCase.isFilmViewed(film.kinopoiskId)
    }
}