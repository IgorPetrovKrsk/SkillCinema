package igor.petrov.final_android_lvl1.presentation.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.DBUpdateState
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionForRecyclerView
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.presentation.adapters.CollectionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {

    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val _DBUpdateState = MutableStateFlow<DBUpdateState>(DBUpdateState.Ready)
    val dBUpdateState = _DBUpdateState.asStateFlow()

    private val _collectionList = MutableStateFlow<List<CollectionsDB>>(emptyList())
    val collectionList = _collectionList.asStateFlow()

    private val _interestedList = MutableStateFlow<List<ItemCollectionForRecyclerView>>(emptyList())
    val interestedList = _interestedList.asStateFlow()
    var interestedListSize = 0

    private val _viewedList = MutableStateFlow<List<ItemCollectionForRecyclerView>>(emptyList())
    val viewedList = _viewedList.asStateFlow()
    var viewedListSize = 0


    //val allCollections: StateFlow<List<CollectionsDB>> = kinopoiskUseCase.getAllCollections().stateIn(
    //    scope = dbScope,
    //    started = SharingStarted.WhileSubscribed(5000L),
    //    initialValue = emptyList()
    //)

    fun getCollections() {
        dbScope.launch {
            _collectionList.value = kinopoiskUseCase.getAllCollections()
        }
    }

    fun getViewed(fullList: Boolean) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            val itemCollectionForRecyclerViewList = mutableListOf<ItemCollectionForRecyclerView>()
            var itemCollectionList = kinopoiskUseCase.getItemCollection(kinopoiskUseCase.viewedCollection)
            viewedListSize = itemCollectionList.size
            if (!fullList) {
                itemCollectionList = itemCollectionList.take(20)
            }
            itemCollectionList.forEach {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(it.collectionId, it.itemType, it.itemId, it.timeUpdated)
                when (it.itemType) {
                    ItemCollectionType.FILM -> {
                        val film = kinopoiskUseCase.getFilm(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (film.nameRu.isNullOrBlank()) film.nameEn else film.nameRu
                        itemCollectionForRecyclerView.itemDescription = film.genres?.joinToString()
                        itemCollectionForRecyclerView.itemRating = film.ratingKinopoisk
                        itemCollectionForRecyclerView.posterUrl = film.posterUrlPreview
                    }

                    ItemCollectionType.PERSON -> {
                        val person = kinopoiskUseCase.getPerson(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (person.nameRu.isNullOrBlank()) person.nameEn else person.nameRu
                        itemCollectionForRecyclerView.itemDescription = "${person.profession ?: "---"}"
                        itemCollectionForRecyclerView.itemRating = null
                        itemCollectionForRecyclerView.posterUrl = person.posterUrl
                    }
                }
                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            if (!fullList) {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(kinopoiskUseCase.viewedCollection.collectionId, ItemCollectionType.FILM, 0, System.currentTimeMillis())
                if (itemCollectionForRecyclerViewList.size == 0) {
                    itemCollectionForRecyclerView.itemName = App.applicationContext().getString(R.string.text_no_films_in_collection)
                    itemCollectionForRecyclerView.posterUrl = R.drawable.ic_folder_empty.toString()
                } else {
                    itemCollectionForRecyclerView.itemName = App.applicationContext().getString(R.string.text_clear_history)
                    itemCollectionForRecyclerView.posterUrl = R.drawable.ic_trash.toString()
                }
                itemCollectionForRecyclerView.itemDescription = ""
                itemCollectionForRecyclerView.itemRating = null
                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            _viewedList.value = itemCollectionForRecyclerViewList
            _DBUpdateState.value = DBUpdateState.Ready
        }
    }

    fun getInterested(fullList: Boolean) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            val itemCollectionForRecyclerViewList = mutableListOf<ItemCollectionForRecyclerView>()
            var itemCollectionList = kinopoiskUseCase.getItemCollection(kinopoiskUseCase.interestedCollection)
            interestedListSize = itemCollectionList.size
            if (!fullList) {
                itemCollectionList = itemCollectionList.take(20)
            }
            itemCollectionList.forEach {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(it.collectionId, it.itemType, it.itemId, it.timeUpdated)
                when (it.itemType) {
                    ItemCollectionType.FILM -> {
                        val film = kinopoiskUseCase.getFilm(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (film.nameRu.isNullOrBlank()) film.nameEn else film.nameRu
                        itemCollectionForRecyclerView.itemDescription = film.genres?.joinToString()
                        itemCollectionForRecyclerView.itemRating = film.ratingKinopoisk
                        itemCollectionForRecyclerView.posterUrl = film.posterUrlPreview
                    }

                    ItemCollectionType.PERSON -> {
                        val person = kinopoiskUseCase.getPerson(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (person.nameRu.isNullOrBlank()) person.nameEn else person.nameRu
                        itemCollectionForRecyclerView.itemDescription = "${person.profession ?: "---"}"
                        itemCollectionForRecyclerView.itemRating = null
                        itemCollectionForRecyclerView.posterUrl = person.posterUrl
                    }
                }
                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            if (!fullList) {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(kinopoiskUseCase.interestedCollection.collectionId, ItemCollectionType.FILM, 0, System.currentTimeMillis())
                if (itemCollectionForRecyclerViewList.size == 0) {
                    itemCollectionForRecyclerView.itemName = App.applicationContext().getString(R.string.text_no_films_in_collection)
                    itemCollectionForRecyclerView.posterUrl = R.drawable.ic_folder_empty.toString()
                } else {
                    itemCollectionForRecyclerView.itemName = App.applicationContext().getString(R.string.text_clear_history)
                    itemCollectionForRecyclerView.posterUrl = R.drawable.ic_trash.toString()
                }
                itemCollectionForRecyclerView.itemDescription = ""
                itemCollectionForRecyclerView.itemRating = null

                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            _interestedList.value = itemCollectionForRecyclerViewList
            _DBUpdateState.value = DBUpdateState.Ready
        }
    }

    fun newCollection(newCollectionName: String, collectionAdapter: CollectionAdapter) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            val newCollectionId = kinopoiskUseCase.newCollection(newCollectionName)
            viewModelScope.launch {
                collectionAdapter.addNewData(CollectionsDBDto(newCollectionName, false, newCollectionId.toInt()))
                _DBUpdateState.value = DBUpdateState.Ready
            }
        }
    }

    fun deleteCollection(collection: CollectionsDB) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            kinopoiskUseCase.deleteCollection(collection)
            _DBUpdateState.value = DBUpdateState.Ready
        }
    }

    fun clearCollection(collectionId: Int) {
        dbScope.launch {
            _DBUpdateState.value = DBUpdateState.Updating
            kinopoiskUseCase.clearCollection(CollectionsDBDto("", false, collectionId))
            getInterested(false)
            getViewed(false)
            _DBUpdateState.value = DBUpdateState.Ready
        }
    }


}