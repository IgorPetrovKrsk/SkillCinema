package igor.petrov.final_android_lvl1.presentation.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film

class FilmPremieresListPagingSource(
    private val kinopoiskUseCase: KinopoiskUseCase,
    private val fullPremieresList: Boolean = false
) :
    PagingSource<Int, Film>() {

    override fun getRefreshKey(state: PagingState<Int, Film>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            if (fullPremieresList) {
                kinopoiskUseCase.getAllFilmPremieresList()
            } else {
                kinopoiskUseCase.getFilmPremieresList()
            }
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
