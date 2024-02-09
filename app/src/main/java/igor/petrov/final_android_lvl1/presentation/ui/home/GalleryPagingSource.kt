package igor.petrov.final_android_lvl1.presentation.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Photo

class GalleryPagingSource(
    private val kinopoiskUseCase: KinopoiskUseCase,
    private val kinopoiskId: Int,
    private val type: PhotoType? = null,
    private val fullList: Boolean = false
) :
    PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            kinopoiskUseCase.getFilmPhotoList(page,kinopoiskId,type)
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
