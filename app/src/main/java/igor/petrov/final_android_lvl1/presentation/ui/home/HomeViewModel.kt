package igor.petrov.final_android_lvl1.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.FilmCollections
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.data.dto.GenresCountriesListDto
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.presentation.adapters.FilmAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) :
    ViewModel() {

    var filmPremierAdapter: FilmAdapter? = null  //адаптер находится во ViewModel чтобы к нему можно было образаться с 2х разных фрагментов

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    var filmRandom1List: Flow<PagingData<Film>> = emptyFlow() //пустые флоу пока система не получит данные о жанрах и странах
    var random1Country = CountryDto(null, "")
    var random1Genre = GenreDto(null, "")

    var filmRandom2List: Flow<PagingData<Film>> = emptyFlow()
    var random2Country = CountryDto(null, "")
    var random2Genre = GenreDto(null, "")
    var genresCountriesListDto = GenresCountriesListDto(emptyList(), emptyList())


    val filmPremieresList: Flow<PagingData<Film>> = Pager(config = PagingConfig(pageSize = 20),
        initialKey = null,
        pagingSourceFactory = {
            FilmPremieresListPagingSource(
                kinopoiskUseCase,
                false
            )
        }).flow.cachedIn(viewModelScope)

    val filmPopularList: Flow<PagingData<Film>> =
        Pager(config = PagingConfig(pageSize = 20), initialKey = null, pagingSourceFactory = {
            FilmCollectionListPagingSource(
                kinopoiskUseCase, FilmCollections.TOP_POPULAR_ALL.name, false
            )
        }).flow.cachedIn(viewModelScope)

    val filmTop250List: Flow<PagingData<Film>> =
        Pager(config = PagingConfig(pageSize = 20), initialKey = null, pagingSourceFactory = {
            FilmCollectionListPagingSource(
                kinopoiskUseCase, FilmCollections.TOP_250_MOVIES.name, false
            )
        }).flow.cachedIn(viewModelScope)

    val serialList: Flow<PagingData<Film>> =
        Pager(config = PagingConfig(pageSize = 20), initialKey = null, pagingSourceFactory = {
            FilmFilterListPagingSource(
                kinopoiskUseCase,
                CountryDto(null, ""),
                GenreDto(null, ""),
                OrderType.RATING,
                FilmType.TV_SERIES,
                0,
                10,
                1000,
                3000,
                null,
                null,
                false
            )
        }).flow.cachedIn(viewModelScope)

    init {
        getGenresAndCountries()
    }

    fun getGenresAndCountries() {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                genresCountriesListDto = kinopoiskUseCase.getGenresCountries()
                if (genresCountriesListDto.genresList.isNotEmpty() && genresCountriesListDto.countriesList.isNotEmpty()) {
                    restartRandom1List()
                    restartRandom2List()
                }
                _loadingState.value = LoadingState.Ready
            }
            catch (e: KinopoiskException){
                _loadingState.value = LoadingState.Error(e)
            }
        }
    }

    fun restartRandom1List() {
            random1Country = genresCountriesListDto.countriesList.get(Random.nextInt(35)) //возьмём только первые 35 стран, чтобы подборки были заполнены
            random1Genre = genresCountriesListDto.genresList.random()
            filmRandom1List = Pager(config = PagingConfig(pageSize = 20),
                initialKey = null,
                pagingSourceFactory = {
                    FilmFilterListPagingSource(
                        kinopoiskUseCase, random1Country, random1Genre,OrderType.RATING,null,0,10,1000,3000,null,null,false
                    )
                }).flow.cachedIn(viewModelScope)
        }

        fun restartRandom2List() {
            random2Country = genresCountriesListDto.countriesList.get(Random.nextInt(35)) //возьмём только первые 35 стран, чтобы подборки были заполнены
            random2Genre = genresCountriesListDto.genresList.random()
            filmRandom2List = Pager(config = PagingConfig(pageSize = 20),
                initialKey = null,
                pagingSourceFactory = {
                    FilmFilterListPagingSource(
                        kinopoiskUseCase, random2Country, random2Genre,OrderType.RATING,null,0,10,1000,3000,null,null,false
                    )
                }).flow.cachedIn(viewModelScope)

        }

    suspend fun isFilmViewed(film: Film):Boolean{
        return kinopoiskUseCase.isFilmViewed(film.kinopoiskId)
    }


}