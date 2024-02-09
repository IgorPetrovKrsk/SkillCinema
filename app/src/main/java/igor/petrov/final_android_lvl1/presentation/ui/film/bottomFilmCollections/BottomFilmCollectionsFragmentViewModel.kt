package igor.petrov.final_android_lvl1.presentation.ui.film.bottomFilmCollections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.DBUpdateState
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.entity.collectionDB.ItemCollectionDB
import igor.petrov.final_android_lvl1.presentation.adapters.BottomCollectionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomFilmCollectionsFragmentViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {

    var kinopoiskId: Int = 0
    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val _DBUpdateState = MutableStateFlow<DBUpdateState>(DBUpdateState.Ready)
    val dBUpdateState = _DBUpdateState.asStateFlow()


    private val _collectionList = MutableStateFlow<List<CollectionsDB>>(emptyList())
    val collectionList = _collectionList.asStateFlow()

    var filmCollections: List<ItemCollectionDB> = emptyList()

    private val _film = MutableStateFlow<Film>(FilmDto(0))
    var film: StateFlow<Film> = _film.asStateFlow()

    fun getFilm() {
        dbScope.launch {
            _film.value = kinopoiskUseCase.getFilm(kinopoiskId)
        }
    }

     fun getCollections() {
        dbScope.launch {
            filmCollections = kinopoiskUseCase.getFilmCollections(kinopoiskId)
            _collectionList.value = kinopoiskUseCase.getAllCollections()
        }
    }

    fun newCollection(newCollectionName: String, bottomCollectionAdapter: BottomCollectionAdapter) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            val newCollectionId = kinopoiskUseCase.newCollection(newCollectionName)
            viewModelScope.launch {
                bottomCollectionAdapter.addNewData(CollectionsDBDto(newCollectionName, false, newCollectionId.toInt()))
                _DBUpdateState.value = DBUpdateState.Ready
            }
        }
    }

    fun addOrRemoveFilmFromCollection(collection: CollectionsDB){
        dbScope.launch {
            kinopoiskUseCase.addOrRemoveFilmFromCollection(kinopoiskId,collection)
        }
    }

}