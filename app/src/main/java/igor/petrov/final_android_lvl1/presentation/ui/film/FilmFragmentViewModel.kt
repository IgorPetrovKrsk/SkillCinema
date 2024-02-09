package igor.petrov.final_android_lvl1.presentation.ui.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.StaffDto
import igor.petrov.final_android_lvl1.data.dto.serialDto.SerialSeasonsListDto
import igor.petrov.final_android_lvl1.data.dto.similarFilmDto.SimilarFilmDto
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.Photo
import igor.petrov.final_android_lvl1.entity.Staff
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.entity.collectionDB.ItemCollectionDB
import igor.petrov.final_android_lvl1.entity.serial.SerialSeasonsList
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm
import igor.petrov.final_android_lvl1.presentation.ui.home.GalleryPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
@HiltViewModel
class FilmFragmentViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val dbScope = CoroutineScope(Dispatchers.IO)
    var galleryList: Flow<PagingData<Photo>> = emptyFlow()

    private val _film = MutableStateFlow<Film>(FilmDto(0))
    var film: StateFlow<Film> = _film.asStateFlow()

    private val _serial = MutableStateFlow<SerialSeasonsList>(SerialSeasonsListDto(0, emptyList()))
    var serial: StateFlow<SerialSeasonsList> = _serial.asStateFlow()


    private val _staffList = MutableStateFlow<List<Staff>>(emptyList<Staff>())

    private val _actorsList = MutableStateFlow<List<Staff>>(emptyList<Staff>())
    var actorsList = _actorsList.asStateFlow()

    private val _crewList = MutableStateFlow<List<Staff>>(emptyList<Staff>())
    var crewList = _crewList.asStateFlow()

    private val _collectionsList = MutableStateFlow<List<ItemCollectionDB>>(emptyList<ItemCollectionDB>())
    var collectionsList = _collectionsList.asStateFlow()

    private val _similarFilmList = MutableStateFlow<List<SimilarFilm>>(emptyList())
    var similarFilmList = _similarFilmList.asStateFlow()

    var kinopoiskId: Int = 0
    var label: String? = null

    private val dBScope = CoroutineScope(Dispatchers.IO)

    fun getFilm() {
        _loadingState.value = LoadingState.Loading
        dBScope.launch {
            try {
                _film.value = kinopoiskUseCase.getFilm(kinopoiskId, true)
                _loadingState.value = LoadingState.Ready
            }catch (e: KinopoiskException){
                _loadingState.value = LoadingState.Error(e)
            }
        }
    }

    fun getStaff() {
        dBScope.launch {
            _staffList.value = kinopoiskUseCase.getStaff(kinopoiskId)
            _actorsList.value = _staffList.value.filter { it.professionKey == "ACTOR" }
            _crewList.value = _staffList.value.filter { it.professionKey != "ACTOR" }

            //crewList = _staffList.filter { it.professionKey!= "ACTOR" }.stateIn(viewModelScope)
        }
    }

    fun getGallery() {
        galleryList = Pager(config = PagingConfig(pageSize = 20),
            initialKey = null,
            pagingSourceFactory = {
                GalleryPagingSource(
                    kinopoiskUseCase,
                    kinopoiskId,
                    null,
                    false
                )
            }).flow.cachedIn(viewModelScope)
    }

    fun getSimilarList() {
        viewModelScope.launch {
            _similarFilmList.value = kinopoiskUseCase.getSimilarFilmList(kinopoiskId)
        }
    }

    fun getSerialSeasons() {
        viewModelScope.launch {
            _serial.value = kinopoiskUseCase.getSerialSeasonsList(kinopoiskId)
        }
    }

    fun getFilmCollectionsList() {
        dbScope.launch {
            _collectionsList.value = kinopoiskUseCase.getFilmCollections(kinopoiskId)
        }
    }

    fun addOrRemoveFilmFromCollection(collection: CollectionsDB) {
        dbScope.launch {
            kinopoiskUseCase.addOrRemoveFilmFromCollection(kinopoiskId, collection)
            getFilmCollectionsList()
        }
    }

    fun getStaffToJson(list: List<Staff>): String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<StaffDto>> = moshi.adapter<List<StaffDto>>()
        return jsonAdapter.toJson(list as List<StaffDto>)
    }

    fun getSimilarFilmToJson(list: List<SimilarFilm>): String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<SimilarFilmDto>> = moshi.adapter<List<SimilarFilmDto>>()
        return jsonAdapter.toJson(list as List<SimilarFilmDto>)
    }

}