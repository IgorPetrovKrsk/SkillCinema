package igor.petrov.final_android_lvl1.presentation.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm

class FilmSimilarListPagingSource(
    private val kinopoiskUseCase: KinopoiskUseCase,
    private val kinopoiskId: Int
) :
    PagingSource<Int, SimilarFilm>() {

    override fun getRefreshKey(state: PagingState<Int, SimilarFilm>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SimilarFilm> {
        //val page = params.key ?: 1
        return kotlin.runCatching {
                kinopoiskUseCase.getSimilarFilmList(kinopoiskId)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = null //if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
}
