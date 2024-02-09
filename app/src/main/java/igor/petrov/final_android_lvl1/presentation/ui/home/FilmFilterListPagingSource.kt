package igor.petrov.final_android_lvl1.presentation.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film

class FilmFilterListPagingSource(
    private val kinopoiskUseCase: KinopoiskUseCase,
    private val country: CountryDto? = null,
    private val genre: GenreDto? = null,
    private val order: OrderType? = null,
    private val type: FilmType? = null,
    private val ratingFrom: Int = 0,
    private val ratingTo: Int = 10,
    private val yearFrom: Int = 1000,
    private val yearTo: Int = 3000,
    private val imdbId: String? = null,
    private val keyword: String? = null,
    private val fullList: Boolean = false
) :
    PagingSource<Int, Film>() {

    override fun getRefreshKey(state: PagingState<Int, Film>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            kinopoiskUseCase.getFilmFilterList(
                page = page,
                country = country,
                genre = genre,
                order = order,
                type = type,
                ratingFrom = ratingFrom,
                ratingTo = ratingTo,
                yearFrom = yearFrom,
                yearTo = yearTo,
                imdbId = imdbId,
                keyword = keyword,
                fullList = fullList
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (!fullList || it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
}
