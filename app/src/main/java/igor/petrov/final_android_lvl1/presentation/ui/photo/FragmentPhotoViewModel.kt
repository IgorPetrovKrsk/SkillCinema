package igor.petrov.final_android_lvl1.presentation.ui.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Photo
import igor.petrov.final_android_lvl1.presentation.ui.home.GalleryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class FragmentPhotoViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) :
    ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    var kinopoiskId: Int = 0
    var photoType = PhotoType.STILL
    var photoList: Flow<PagingData<Photo>> = emptyFlow()

    fun getPhotoList(){
        photoList = Pager(config = PagingConfig(pageSize = 20),
            initialKey = null,
            pagingSourceFactory = {
                GalleryPagingSource(
                    kinopoiskUseCase,
                    kinopoiskId,
                    photoType,
                    true
                )
            }).flow.cachedIn(viewModelScope)
    }


}
